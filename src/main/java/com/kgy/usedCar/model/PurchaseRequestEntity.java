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
@Table(name = "purchase_request")
public class PurchaseRequestEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "used_car_id")
    private UsedCarEntity usedCar;

    public static PurchaseRequestEntity of(UserEntity user, UsedCarEntity usedCar) {
        return PurchaseRequestEntity.builder()
                .user(user)
                .usedCar(usedCar)
                .build();
    }
}
