package com.osproject.microservices.fund.service;

import com.osproject.microservices.fund.dto.FundDto;
import com.osproject.microservices.fund.dto.FundInfo;
import com.osproject.microservices.fund.dto.response.FundResponse;

import java.util.List;

public interface FundService {

    FundResponse createFund(FundDto fundDto);

    List<FundInfo> getFunds(int fundId);

    FundInfo getById(int id);

    FundResponse updateFund(int id, FundDto fundDto);

    FundResponse deleteFund(int id);


}
