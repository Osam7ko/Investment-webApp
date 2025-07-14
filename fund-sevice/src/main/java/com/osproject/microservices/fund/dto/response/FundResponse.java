package com.osproject.microservices.fund.dto.response;

import com.osproject.microservices.fund.dto.FundInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FundResponse {

    private String responseCode;
    private String responseMessage;
    private FundInfo fundInfo;
}
