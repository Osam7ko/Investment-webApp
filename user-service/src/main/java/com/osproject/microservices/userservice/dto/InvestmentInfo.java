package com.osproject.microservices.userservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvestmentInfo {
    private String accountName;

    private String investmentNumber;

    private Integer fundId;
    private double amount;
    private double unitsOfPurchased;
    private double currentInvestmentValue;

}