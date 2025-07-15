package com.osproject.microservices.pricing.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/price")
public class pricingController {

    @GetMapping("/{fundId}")
    @ResponseStatus(HttpStatus.OK)
    public double getPrice(@PathVariable String fundId){
        double price = 0.0;
        int id = Integer.parseInt(fundId);
        if(id < 10){
            price = 50.50;
        }else if(id == 10){
            price = 100.00;
        }
        return price;
    }
}
