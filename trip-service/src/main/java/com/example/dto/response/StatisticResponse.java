package com.example.dto.response;

public record StatisticResponse(
        String date,
        Long tripCount,
        Double averagePrice) {
}
