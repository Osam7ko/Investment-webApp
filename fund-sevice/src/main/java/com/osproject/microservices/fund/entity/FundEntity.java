package com.osproject.microservices.fund.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int fundId;

    private String fundName;
    private String description;
    private String assetType;
    private String assetTypeSubCategory;

    private double expenseRatio;
    private double nav;
}
