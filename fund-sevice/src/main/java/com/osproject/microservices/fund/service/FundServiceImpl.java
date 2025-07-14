package com.osproject.microservices.fund.service;

import com.osproject.microservices.fund.dto.FundDto;
import com.osproject.microservices.fund.dto.FundInfo;
import com.osproject.microservices.fund.dto.response.FundResponse;
import com.osproject.microservices.fund.entity.FundEntity;
import com.osproject.microservices.fund.repository.FundRepository;
import com.osproject.microservices.fund.utils.FundUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FundServiceImpl implements FundService {

    private final FundRepository fundRepository;


    @Override
    public FundResponse createFund(FundDto fundDto) {
        /**
         * Check the fund id
         * create new fund
         */

        if (fundRepository.existsById(fundDto.fundId())) {
            return FundResponse.builder()
                    .responseCode(FundUtils.FUND_EXISTS_CODE)
                    .responseMessage(FundUtils.FUND_EXISTS_MESSAGE)
                    .fundInfo(null)
                    .build();
        }

        FundEntity newFund = FundEntity.builder()
                .fundName(fundDto.fundName())
                .description(fundDto.description())
                .assetType(fundDto.assetType())
                .assetTypeSubCategory(fundDto.assetTypeSubCategory())
                .expenseRatio(fundDto.expenseRatio())
                .nav(fundDto.nav())
                .build();

        FundEntity savedFund = fundRepository.save(newFund);

        return FundResponse.builder()
                .responseCode(FundUtils.FUND_CREATION_CODE)
                .responseMessage(FundUtils.FUND_CREATION_MESSAGE)
                .fundInfo(FundInfo.builder()
                        .fundName(savedFund.getFundName())
                        .fundId(savedFund.getFundId())
                        .description(savedFund.getDescription())
                        .assetType(savedFund.getAssetType())
                        .assetTypeSubCategory(savedFund.getAssetTypeSubCategory())
                        .expenseRatio(savedFund.getExpenseRatio())
                        .nav(savedFund.getNav())
                        .build())
                .build();
    }

    @Override
    public List<FundInfo> getFunds(int fundId) {
        if (fundId == -1) {
            return fundRepository.findAll().stream()
                    .map(this::mapToFundInfo)
                    .collect(Collectors.toList());
        } else {
            FundEntity fund = fundRepository.findById(fundId)
                    .orElseThrow(() -> new RuntimeException("Fund not found"));
            return List.of(mapToFundInfo(fund));
        }
    }

    @Override
    public FundInfo getById(int id) {
        FundEntity fund = fundRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fund not found"));
        return mapToFundInfo(fund);
    }

    @Transactional
    @Override
    public FundResponse deleteFund(int id) {

        if (!fundRepository.existsById(id)) {
            return FundResponse.builder()
                    .responseCode(FundUtils.FUND_NOT_EXISTS_CODE)
                    .responseMessage(FundUtils.FUND_NOT_EXISTS_MESSAGE)
                    .fundInfo(null)
                    .build();
        }

        FundEntity fund = fundRepository.findById(id).orElseThrow();
        fundRepository.deleteById(id);

        return FundResponse.builder()
                .responseCode(FundUtils.FUND_DELETE_CODE)
                .responseMessage(FundUtils.FUND_DELETE_MESSAGE)
                .fundInfo(FundInfo.builder()
                        .fundName(fund.getFundName())
                        .fundId(fund.getFundId())
                        .build())
                .build();
    }

    private FundInfo mapToFundInfo(FundEntity fund) {
        return FundInfo.builder()
                .fundId(fund.getFundId())
                .fundName(fund.getFundName())
                .description(fund.getDescription())
                .assetType(fund.getAssetType())
                .assetTypeSubCategory(fund.getAssetTypeSubCategory())
                .expenseRatio(fund.getExpenseRatio())
                .nav(fund.getNav())
                .build();
    }
}
