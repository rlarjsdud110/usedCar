package com.kgy.usedCar.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "consult_image")
public class ConsultImageEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "consult_id", nullable = false)
    private ConsultEntity consult;

    public static ConsultImageEntity of(String imageUrl, ConsultEntity entity){
        return ConsultImageEntity.builder()
                .imageUrl(imageUrl)
                .consult(entity)
                .build();
    }

}
