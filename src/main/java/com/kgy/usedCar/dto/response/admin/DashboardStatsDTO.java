package com.kgy.usedCar.dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DashboardStatsDTO {
    private long vehicleCount;
    private long totalUsersCount;
    private long unresolvedConsultCount;
    private long totalNoticesCount;

    public static DashboardStatsDTO of(long vehicleCount, long totalUsersCount, long unresolvedConsultCount, long totalNoticesCount){
        return new DashboardStatsDTO(
                vehicleCount,
                totalUsersCount,
                unresolvedConsultCount,
                totalNoticesCount
        );
    }
}
