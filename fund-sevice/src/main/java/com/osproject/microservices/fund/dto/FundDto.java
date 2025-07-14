package com.osproject.microservices.fund.dto;

public record FundDto(int fundId, String fundName, String description,
                      String assetType, String assetTypeSubCategory,
                      double expenseRatio, double nav) {
}