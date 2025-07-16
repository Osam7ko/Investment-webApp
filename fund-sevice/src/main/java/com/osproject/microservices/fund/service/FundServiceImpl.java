package com.osproject.microservices.fund.service;

import com.osproject.microservices.fund.dto.FundDto;
import com.osproject.microservices.fund.dto.FundInfo;
import com.osproject.microservices.fund.dto.response.FundResponse;
import com.osproject.microservices.fund.entity.FundEntity;
import com.osproject.microservices.fund.repository.FundRepository;
import com.osproject.microservices.fund.utils.FundUtils;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
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

        FundEntity newFund = buildFundEntityFromDto(fundDto);

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
                .fundInfo(mapToFundInfo(savedFund))
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
                .fundInfo(mapToFundInfo(fallbackFund))
                .build();
    }

    @Override
    @Cacheable(value = "fund", key = "#fundId", condition = "#fundId > 1")
    @Transactional(readOnly = true)
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
    @Cacheable(value = "fund", key = "#id")
    @Transactional(readOnly = true)
    public FundInfo getById(int id) {
        FundEntity fund = fundRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fund not found"));
        return mapToFundInfo(fund);
    }

    @Override
    @CachePut(value = "fund", key = "#id")
    public FundResponse updateFund(int id, FundDto fundDto) {
        /**
         * Check the fund id
         * create new fund
         */
        if (!fundRepository.existsById(id)) {
            return FundResponse.builder()
                    .responseCode(FundUtils.FUND_NOT_EXISTS_CODE)
                    .responseMessage(FundUtils.FUND_NOT_EXISTS_MESSAGE)
                    .fundInfo(null)
                    .build();
        }

        FundEntity fund = fundRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fund not found"));

        fund.setFundName(fundDto.fundName());
        fund.setDescription(fundDto.description());
        fund.setAssetType(fund.getAssetType());
        fund.setAssetTypeSubCategory(fundDto.assetTypeSubCategory());

        FundEntity savedFund = fundRepository.save(fund);

        return FundResponse.builder()
                .responseCode(FundUtils.FUND_UPDATE_CODE)
                .responseMessage(FundUtils.FUND_UPDATE_MESSAGE)
                .fundInfo(mapToFundInfo(savedFund))
                .build();
    }

    @Transactional
    @Override
    @Cacheable(value = "fund", key = "#id")
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

    private FundEntity buildFundEntityFromDto(FundDto dto) {
        return FundEntity.builder()
                .fundName(dto.fundName())
                .description(dto.description())
                .assetType(dto.assetType())
                .assetTypeSubCategory(dto.assetTypeSubCategory())
                .expenseRatio(dto.expenseRatio())
                .build();
    }

    // While the cache application is SIMPLE
    @Scheduled(fixedRate = 20000)
    @CacheEvict(value = "fund", allEntries = true)
    public void clearFundCache() {
        System.out.println("Cache is cleared now");
    }
}
