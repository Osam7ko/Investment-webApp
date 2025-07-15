package com.osproject.microservices.userservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.osproject.microservices.userservice.dto.*;
import com.osproject.microservices.userservice.dto.response.AccountResponse;
import com.osproject.microservices.userservice.dto.response.InvestmentResponse;
import com.osproject.microservices.userservice.dto.response.UserResponse;
import com.osproject.microservices.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    // User Api's

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String getUserName(@PathVariable Long id) {
        return userService.getUserName(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public UserResponse deleteUser(@RequestParam Long id) {
        return userService.deleteUser(id);
    }

    // Account Api's

    @PostMapping("/account/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse createAccount(@PathVariable Long id, @RequestBody AccountDto accountDto) {
        return userService.createAccount(id, accountDto);
    }

    @GetMapping("/accounts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<AccountInfo> getUserAccounts(@PathVariable Long id) {
        return userService.getAccounts(id);
    }

    @DeleteMapping("/account/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountResponse deleteAccount(
            @PathVariable Long id,
            @RequestParam String accountNumber) {
        return userService.deleteAccount(id, accountNumber);
    }


    // Investment Api's

    @PostMapping("/investment/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public InvestmentResponse createInvestment(@PathVariable Long id, @RequestBody InvestmentDto investmentDto) throws JsonProcessingException {
        return userService.createInvestment(id, investmentDto);
    }

    @GetMapping("/investments/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<InvestmentInfo> getUserInvestments(@PathVariable Long id) {
        return userService.getInvestments(id);
    }

    @DeleteMapping("/investment/{id}")
    @ResponseStatus(HttpStatus.OK)
    public InvestmentResponse deleteInvestment(
            @PathVariable Long id,
            @RequestParam String investmentNumber) {
        return userService.deleteInvestment(id, investmentNumber);
    }
}
