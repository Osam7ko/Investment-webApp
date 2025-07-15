package com.osproject.microservices.fund.service;

import com.osproject.microservices.fund.dto.FundDto;
import com.osproject.microservices.fund.dto.FundInfo;
import com.osproject.microservices.fund.dto.response.FundResponse;
import com.osproject.microservices.fund.entity.FundEntity;
import com.osproject.microservices.fund.repository.FundRepository;
import com.osproject.microservices.fund.utils.FundUtils;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FundServiceImpl implements FundService {

    private final FundRepository fundRepository;

    private final RestTemplate restTemplate;


    @Override
    @CircuitBreaker(name = "pricingService", fallbackMethod = "getDefaultPrice")
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
                .build();

        try {
            String url = "http://PRICING-SERVICE/api/v1/price/" + fundDto.fundId();
            Double price = restTemplate.getForObject(url, Double.class);
            newFund.setNav(price);
        } catch (Exception e) {
            throw new RuntimeException("Pricing service is not avaliable");
        }

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

    // Fallback method
    private FundResponse getDefaultPrice(FundDto fundDto, Throwable t) {
        FundEntity fallbackFund = FundEntity.builder()
                .fundName(fundDto.fundName())
                .description(fundDto.description())
                .assetType(fundDto.assetType())
                .assetTypeSubCategory(fundDto.assetTypeSubCategory())
                .expenseRatio(fundDto.expenseRatio())
                .nav(0.0)
                .build();

        return FundResponse.builder()
                .responseCode("FALLBACK")
                .responseMessage("Pricing service unavailable, using default NAV")
                .fundInfo(FundInfo.builder()
                        .fundName(fallbackFund.getFundName())
                        .fundId(fallbackFund.getFundId())
                        .description(fallbackFund.getDescription())
                        .assetType(fallbackFund.getAssetType())
                        .assetTypeSubCategory(fallbackFund.getAssetTypeSubCategory())
                        .expenseRatio(fallbackFund.getExpenseRatio())
                        .nav(fallbackFund.getNav())
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
