package com.kgy.usedCar.dto.response.car;

import com.kgy.usedCar.model.CarOptionEntity;
import com.kgy.usedCar.model.UsedCarEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class CarDetailResponseDto {
    private Long id;                    // 등록번호
    private String model;               // 모델명
    private String carType;             // 연료
    private String transmission;        // 변속기
    private String licensePlate;        // 번호판
    private int engine;                 // 엔진 1600 cc
    private String color;               // 차 색상
    private int modelYear;              // 년식
    private int mileage;                // 주행거리
    private int price;                  // 기존가격
    private int discountedPrice;        // 할인된 가격
    private String fuelType;            // 연료 유형
    private boolean isHotDeal = false;  // 이벤트
    private int viewCount;              // 조회수

    private boolean frontRearSensor;    //전후방 센서
    private boolean rearSensor;         //후방 센서
    private boolean frontSensor;        //전방 센서
    private boolean heatedSeat;         //열선 시트
    private boolean ventilatedSeat;     //통풍 시트
    private boolean smartKey;           //스마트키
    private boolean navigation;         //내비
    private boolean ledHeadlight;       //LED 라이트
    private boolean sunroof;            //썬루프

    private List<String> imagesUrl;

    public static CarDetailResponseDto fromEntity(UsedCarEntity usedCarEntity, CarOptionEntity carOptionsEntity, List<String> imagesUrl) {
        return CarDetailResponseDto.builder()
                .id(usedCarEntity.getId())
                .model(usedCarEntity.getModel())
                .carType(usedCarEntity.getCarType())
                .transmission(usedCarEntity.getTransmission())
                .licensePlate(usedCarEntity.getLicensePlate())
                .engine(usedCarEntity.getEngine())
                .color(usedCarEntity.getColor())
                .modelYear(usedCarEntity.getModelYear())
                .mileage(usedCarEntity.getMileage())
                .price(usedCarEntity.getPrice())
                .discountedPrice(usedCarEntity.getDiscountedPrice())
                .fuelType(usedCarEntity.getFuelType())
                .isHotDeal(usedCarEntity.isHotDeal())
                .viewCount(usedCarEntity.getViewCount())
                .frontRearSensor(carOptionsEntity.isFrontRearSensor())
                .rearSensor(carOptionsEntity.isRearSensor())
                .frontSensor(carOptionsEntity.isFrontSensor())
                .heatedSeat(carOptionsEntity.isHeatedSeat())
                .ventilatedSeat(carOptionsEntity.isVentilatedSeat())
                .smartKey(carOptionsEntity.isSmartKey())
                .navigation(carOptionsEntity.isNavigation())
                .ledHeadlight(carOptionsEntity.isLedHeadlight())
                .sunroof(carOptionsEntity.isSunroof())
                .imagesUrl(imagesUrl)
                .build();
    }
}
