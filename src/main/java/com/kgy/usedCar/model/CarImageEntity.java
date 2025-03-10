package com.kgy.usedCar.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "car_images")
public class CarImageEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "used_car_id")
    private Long usedCarId;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "image_type")
    private String imageType;

    public static CarImageEntity fromDto(Long usedCarId, String imageUrl, String imageType){
        return CarImageEntity.builder()
                .usedCarId(usedCarId)
                .imageUrl(imageUrl)
                .imageType(imageType)
                .build();
    }

}
