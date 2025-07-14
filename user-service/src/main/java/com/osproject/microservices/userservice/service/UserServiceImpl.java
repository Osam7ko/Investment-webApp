package com.osproject.microservices.userservice.service;

import com.osproject.microservices.userservice.dto.*;
import com.osproject.microservices.userservice.dto.response.AccountResponse;
import com.osproject.microservices.userservice.dto.response.InvestmentResponse;
import com.osproject.microservices.userservice.dto.response.UserResponse;
import com.osproject.microservices.userservice.entity.Account;
import com.osproject.microservices.userservice.entity.Investment;
import com.osproject.microservices.userservice.entity.UserEntity;
import com.osproject.microservices.userservice.repository.UserRepository;
import com.osproject.microservices.userservice.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final WebClient webClient;


    @Override
    public String getUserName(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not  Found"));

        return userEntity.getUserName();
    }

    @Override
    public UserResponse createUser(UserDto userDto) {
        /**
         * Check if the user exists
         * Create useer with sending a response
         */

        if (userRepository.existsByEmail(userDto.email())) {
            return UserResponse.builder()
                    .responseCode(AccountUtils.USER_EXISTS_CODE)
                    .responseMessage(AccountUtils.USER_EXISTS_MESSAGE)
                    .userInfo(null)
                    .build();
        }

        UserEntity newUser = UserEntity.builder()
                .userName(userDto.userName())
                .password(userDto.password())
                .email(userDto.email())
//                .accountNumber(AccountUtils.generateAccountNumber())
                .build();

        UserEntity savedUser = userRepository.save(newUser);

        return UserResponse.builder()
                .responseCode(AccountUtils.USER_CREATION_CODE)
                .responseMessage(AccountUtils.USER_CREATION_MESSAGE)
                .userInfo(UserInfo.builder()
                        .accountName(savedUser.getUserName())
                        .userId(savedUser.getId())
                        .build())
                .build();
    }

    @Transactional
    @Override
    public UserResponse deleteUser(Long id) {

        if (!userRepository.existsById(id)) {
            return UserResponse.builder()
                    .responseCode(AccountUtils.USER_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.USER_NOT_EXISTS_MESSAGE)
                    .userInfo(null)
                    .build();
        }

        UserEntity user = userRepository.findById(id).orElseThrow();
        userRepository.deleteById(id);

        return UserResponse.builder()
                .responseCode(AccountUtils.USER_DELETE_CODE)
                .responseMessage(AccountUtils.USER_DELETE_MESSAGE)
                .userInfo(UserInfo.builder()
                        .accountName(user.getUserName())
                        .userId(user.getId())
                        .build())
                .build();
    }

    @Override
    public AccountResponse createAccount(Long id, AccountDto accountDto) {
        /**
         * Check if the user exists,
         * check if the account exists
         * Create Account for the user with sending a response
         */

        if (!userRepository.existsById(id)) {
            return AccountResponse.builder()
                    .responseCode(AccountUtils.USER_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.USER_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        UserEntity userAccount = userRepository.findById(id).orElseThrow();
        List<Account> accounts = userAccount.getAccount();
        if (accounts == null) accounts = new ArrayList<>();

        Account newAccount = Account.builder()
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountType(accountDto.accountType())
                .balance(0)
//                .accountNumber(AccountUtils.generateAccountNumber())
                .build();

        accounts.add(newAccount);
        userAccount.setAccount(accounts);

        userRepository.save(userAccount);

        return AccountResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(newAccount.getAccountNumber())
                        .accountName(userAccount.getUserName())
                        .balance(newAccount.getBalance())
                        .accountType(newAccount.getAccountType())
                        .build())
                .build();
    }


    @Override
    public List<AccountInfo> getAccounts(Long userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getAccount()
                .stream()
                .map(account -> AccountInfo.builder()
                        .accountNumber(account.getAccountNumber())
                        .accountName(user.getUserName())
                        .balance(account.getBalance())
                        .accountType(account.getAccountType())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public AccountResponse deleteAccount(Long id, String accountNumber) {
        /**
         * Check if the user exists,
         * check if the account exists
         * Create Account for the user with sending a response
         */


        if (!userRepository.existsById(id)) {
            return AccountResponse.builder()
                    .responseCode(AccountUtils.USER_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.USER_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // find the account
        List<Account> updatedAccounts = user.getAccount()
                .stream()
                .filter(account -> !account.getAccountNumber().equals(accountNumber))
                .collect(Collectors.toList());

        if (updatedAccounts.size() == user.getAccount().size()) {
            return AccountResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
                    .responseMessage("Account number not found for this user")
                    .accountInfo(null)
                    .build();
        }

        user.setAccount(updatedAccounts);
        userRepository.save(user);

        return AccountResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_DELETE_CODE)
                .responseMessage("Account deleted successfully")
                .accountInfo(AccountInfo.builder()
                        .accountName(user.getUserName())
                        .build())
                .build();
    }

    // Investment

    @Override
    public InvestmentResponse createInvestment(Long id, InvestmentDto investmentDto) {
        /**
         * Check if the user exists,
         * check if the account exists
         * Create Investment for the user with sending a response
         */

        if (!userRepository.existsById(id)) {
            return InvestmentResponse.builder()
                    .responseCode(AccountUtils.USER_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.USER_NOT_EXISTS_MESSAGE)
                    .investmentInfo(null)
                    .build();
        }

        UserEntity userAccount = userRepository.findById(id).orElseThrow();
        List<Investment> investments = userAccount.getInvestment();
        if (investments == null) investments = new ArrayList<>();

        Investment newInvestment = Investment.builder()
                .investmentNumber(AccountUtils.generateInvestmentNumber())
                .fundId(investmentDto.fundId())
                .amount(investmentDto.amount())
                .build();

        // Call Fund Api's
        FundDto fundInfo = webClient.get()
                .uri("http://localhost:8081/api/v1/fund/" + newInvestment.getFundId())
                .retrieve()
                .bodyToMono(FundDto.class)
                .block();
        double unitOfPurchased = newInvestment.getAmount() / fundInfo.nav();
        newInvestment.setUnitsOfPurchased(unitOfPurchased);
        newInvestment.setCurrentInvestmentValue(unitOfPurchased * fundInfo.nav());

        investments.add(newInvestment);
        userAccount.setInvestment(investments);

        userRepository.save(userAccount);

        return InvestmentResponse.builder()
                .responseCode(AccountUtils.INVESTMENT_CREATION_CODE)
                .responseMessage(AccountUtils.INVESTMENT_CREATION_MESSAGE)
                .investmentInfo(InvestmentInfo.builder()
                        .accountName(userAccount.getUserName())
                        .fundId(newInvestment.getFundId())
                        .investmentNumber(newInvestment.getInvestmentNumber())
                        .amount(newInvestment.getAmount())
                        .currentInvestmentValue(newInvestment.getCurrentInvestmentValue())
                        .unitsOfPurchased(newInvestment.getUnitsOfPurchased())
                        .build())
                .build();
    }

    @Override
    public List<InvestmentInfo> getInvestments(Long userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getInvestment()
                .stream()
                .map(investment -> InvestmentInfo.builder()
                        .investmentNumber(investment.getInvestmentNumber())
                        .accountName(user.getUserName())
                        .amount(investment.getAmount())
                        .unitsOfPurchased(investment.getUnitsOfPurchased())
                        .currentInvestmentValue(investment.getCurrentInvestmentValue())
                        .fundId(investment.getFundId())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public InvestmentResponse deleteInvestment(Long id, String investmentNumber) {
        /**
         * Check if the user exists,
         * check if the investment exists
         */

        if (!userRepository.existsById(id)) {
            return InvestmentResponse.builder()
                    .responseCode(AccountUtils.USER_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.USER_NOT_EXISTS_MESSAGE)
                    .investmentInfo(null)
                    .build();
        }

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // find the investment
        List<Investment> updatedInvestment = user.getInvestment()
                .stream()
                .filter(investment -> !investment.getInvestmentNumber().equals(investmentNumber))
                .collect(Collectors.toList());

        if (updatedInvestment.size() == user.getInvestment().size()) {
            return InvestmentResponse.builder()
                    .responseCode(AccountUtils.INVESTMENT_NOT_FOUND_CODE)
                    .responseMessage("Investment number not found for this user")
                    .investmentInfo(null)
                    .build();
        }

        user.setInvestment(updatedInvestment);
        userRepository.save(user);

        return InvestmentResponse.builder()
                .responseCode(AccountUtils.INVESTMENT_DELETE_CODE)
                .responseMessage("Account deleted successfully")
                .investmentInfo(InvestmentInfo.builder()
                        .accountName(user.getUserName())
                        .build())
                .build();
    }
}
