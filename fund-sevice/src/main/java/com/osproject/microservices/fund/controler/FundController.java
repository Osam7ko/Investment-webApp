package com.osproject.microservices.fund.controler;

import com.osproject.microservices.fund.dto.FundDto;
import com.osproject.microservices.fund.dto.FundInfo;
import com.osproject.microservices.fund.dto.response.FundResponse;
import com.osproject.microservices.fund.service.FundService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fund")
@RequiredArgsConstructor
public class FundController {

    private final FundService fundService;

    private final CacheManager cacheManager;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FundResponse createFund(@RequestBody FundDto fundDto) {
        return fundService.createFund(fundDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FundInfo> getAllFunds(@RequestParam(defaultValue = "-1") int fundId) {
        return fundService.getFunds(fundId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FundInfo getFundById(@PathVariable int id) {
        return fundService.getById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FundResponse updateFund(@PathVariable int id, @RequestBody FundDto fundDto) {
        return fundService.updateFund(id, fundDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FundResponse deleteFund(@PathVariable int id) {
        return fundService.deleteFund(id);
    }

    @GetMapping("/getCache")
    public Cache getCacheInfo() {
        return cacheManager.getCache("fund");
    }
}
