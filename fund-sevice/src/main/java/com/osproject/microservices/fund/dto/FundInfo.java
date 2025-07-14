package com.osproject.microservices.fund.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FundInfo {

    private String fundName;
    private int fundId;
    private String description;
    private String assetType;
    private String assetTypeSubCategory;

    private double expenseRatio;
    private double nav;
}
