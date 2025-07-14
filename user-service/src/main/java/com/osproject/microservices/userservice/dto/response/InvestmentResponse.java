package com.osproject.microservices.userservice.dto.response;

import com.osproject.microservices.userservice.dto.InvestmentInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvestmentResponse {

    private String responseCode;
    private String responseMessage;
    private InvestmentInfo investmentInfo;
}
