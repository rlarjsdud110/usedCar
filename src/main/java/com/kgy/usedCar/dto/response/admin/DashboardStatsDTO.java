package com.kgy.usedCar.dto.response.admin;

import com.kgy.usedCar.model.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DashboardStatsDTO {
    private long vehicleCount;
    private long totalUsersCount;
    private long unresolvedConsultCount;
    private long totalNoticesCount;

    public static DashboardStatsDTO of(long vehicleCount, long totalUsersCount, long unresolvedConsultCount,
                                       long totalNoticesCount)
    {
        return new DashboardStatsDTO(
                vehicleCount,
                totalUsersCount,
                unresolvedConsultCount,
                totalNoticesCount
        );
    }
}
