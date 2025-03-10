package com.kgy.usedCar.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.kgy.usedCar.model.CarImageEntity;
import com.kgy.usedCar.repository.CarImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;
    private final CarImageRepository carImageRepository;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    public void uploadImage(MultipartFile[] files, Long carId, List<String> imageTypes) throws IOException {
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            String imageType = imageTypes.get(i);

            String fileName = carId + "/" + imageType + ".jpg";

            InputStream inputStream = file.getInputStream();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, inputStream, metadata));

            String imageUrl = amazonS3.getUrl(bucketName, fileName).toString();
            carImageRepository.save(CarImageEntity.fromDto(carId, imageUrl, imageType));
        }
    }

    public boolean deleteImage(Long carId) {
        String folderPrefix = carId + "/";

        ListObjectsV2Request listObjectsRequest = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(folderPrefix);

        ListObjectsV2Result listObjectsResult = amazonS3.listObjectsV2(listObjectsRequest);
        List<S3ObjectSummary> objectSummaries = listObjectsResult.getObjectSummaries();

        if (!objectSummaries.isEmpty()) {
            List<DeleteObjectsRequest.KeyVersion> keysToDelete = new ArrayList<>();

            for (S3ObjectSummary objectSummary : objectSummaries) {
                keysToDelete.add(new DeleteObjectsRequest.KeyVersion(objectSummary.getKey()));
            }

            DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName)
                    .withKeys(keysToDelete);
            amazonS3.deleteObjects(deleteObjectsRequest);
        } else{
            return false;
        }
        return true;
    }

}
