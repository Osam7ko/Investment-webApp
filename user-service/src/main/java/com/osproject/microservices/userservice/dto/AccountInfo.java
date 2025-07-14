package com.osproject.microservices.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountInfo {

    private String accountName;

    private String accountNumber;

    private double balance;

    private String accountType;

}
