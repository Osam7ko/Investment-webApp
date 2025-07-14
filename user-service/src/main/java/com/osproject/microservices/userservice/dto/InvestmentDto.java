package com.osproject.microservices.userservice.dto;

public record InvestmentDto(Long id,
                            Integer fundId,
                            double amount,
                            double unitsOfPurchased,
                            double currentInvestmentValue) {
}
