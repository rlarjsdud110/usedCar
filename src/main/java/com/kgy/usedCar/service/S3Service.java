package com.kgy.usedCar.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.kgy.usedCar.model.CarImageEntity;
import com.kgy.usedCar.model.ConsultEntity;
import com.kgy.usedCar.model.ConsultImageEntity;
import com.kgy.usedCar.model.UsedCarEntity;
import com.kgy.usedCar.repository.CarImageRepository;
import com.kgy.usedCar.repository.ConsultImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;
    private final CarImageRepository carImageRepository;
    private final ConsultImageRepository consultImageRepository;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;


    public void uploadCarImages(MultipartFile[] files, Long carId, List<String> imageTypes) throws IOException {
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            String imageType = imageTypes.get(i);
            String fileName = carId + "/" + imageType + ".jpg";

            String imageUrl = uploadToS3(file, fileName);
            carImageRepository.save(CarImageEntity.fromDto(carId, imageUrl, imageType));
        }
    }

    public void uploadConsultImages(MultipartFile[] files, ConsultEntity entity) throws IOException {
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            String fileName = "consult_" + entity.getId() + "/" + i + ".jpg";

            String imageUrl = uploadToS3(file, fileName);
            consultImageRepository.save(ConsultImageEntity.of(imageUrl, entity));
        }
    }

    public void deleteCarImages(Long carId) {
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

            List<CarImageEntity> usedCarEntity = carImageRepository.findByUsedCarId(carId);
            carImageRepository.deleteAll(usedCarEntity);
        }
    }

    public void deleteConsultImages(ConsultEntity entity) {
        String folderPrefix = "consult_" + entity.getId() + "/";

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

            List<ConsultImageEntity> imagesToDelete = consultImageRepository.findByConsultId(entity.getId());
            consultImageRepository.deleteAll(imagesToDelete);
        }
    }

    private String uploadToS3(MultipartFile file, String fileName) throws IOException {
        InputStream inputStream = file.getInputStream();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, inputStream, metadata));

        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    public void updateCarImages(MultipartFile[] files, Long carId, List<String> imageTypes) throws IOException {
        deleteCarImages(carId);
        uploadCarImages(files, carId, imageTypes);
    }

    public void updateConsultImages(MultipartFile[] files, ConsultEntity entity) throws IOException {
        deleteConsultImages(entity);
        uploadConsultImages(files, entity);
    }
}
