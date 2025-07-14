package com.osproject.microservices.userservice.service;

import com.osproject.microservices.userservice.dto.*;
import com.osproject.microservices.userservice.dto.response.AccountResponse;
import com.osproject.microservices.userservice.dto.response.InvestmentResponse;
import com.osproject.microservices.userservice.dto.response.UserResponse;

import java.util.List;

public interface UserService {


    String getUserName(Long id);

    UserResponse createUser(UserDto userDto);

    UserResponse deleteUser(Long id);

    AccountResponse createAccount(Long id, AccountDto accountDto);

    List<AccountInfo> getAccounts(Long userId);

    AccountResponse deleteAccount(Long id, String accountNumber);

    InvestmentResponse createInvestment(Long id, InvestmentDto accountDto);

    List<InvestmentInfo> getInvestments(Long userId);

    InvestmentResponse deleteInvestment(Long id, String investmentNumber);
}
