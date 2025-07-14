package com.osproject.microservices.userservice.utils;

import java.time.Year;

public class AccountUtils {

    // Creation Message
    public static final String USER_EXISTS_CODE = "001";
    public static final String USER_EXISTS_MESSAGE = "The user is already exists!";
    public static final String USER_CREATION_CODE = "002";
    public static final String USER_CREATION_MESSAGE = "User Has Been Created Successfully!";
    public static final String USER_NOT_EXISTS_CODE = "003";
    public static final String USER_NOT_EXISTS_MESSAGE = "The user is not exists!";
    public static final String USER_DELETE_CODE = "004";
    public static final String USER_DELETE_MESSAGE = "User With it's Related accounts has been Deleted Successfully!";

    // Accounts
    public static final String ACCOUNT_EXISTS_CODE = "005";
    public static final String ACCOUNT_EXISTS_MESSAGE = "The Account is already exists!";
    public static final String ACCOUNT_CREATION_CODE = "006";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account Has Been Created Successfully!";
    public static final String ACCOUNT_NOT_FOUND_CODE = "007";
    public static final String ACCOUNT_DELETE_CODE = "008";

    // Investment
    public static final String INVESTMENT_NOT_FOUND_CODE = "011";
    public static final String INVESTMENT_DELETE_CODE = "012";
    public static final String INVESTMENT_CREATION_CODE = "009";
    public static final String INVESTMENT_CREATION_MESSAGE = "010";

    public static String generateAccountNumber() {
        /**
         * 2025 + random six digit
         */

        Year currentYear = Year.now();
        int min = 100000;
        int max = 599999;

        // Generate a random number between min and max
        int randNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);

        // convert the year into a string
        String year = String.valueOf(currentYear);
        String randomNumber = String.valueOf(randNumber);
        StringBuilder accountNumber = new StringBuilder();

        return accountNumber.append(year).append(randomNumber).toString();
    }

    // Investment number
    public static String generateInvestmentNumber() {
        /**
         * 2025 + random six digit
         */

        Year currentYear = Year.now();
        int min = 600000;
        int max = 999999;

        // Generate a random number between min and max
        int randNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);

        // convert the year into a string
        String year = String.valueOf(currentYear);
        String randomNumber = String.valueOf(randNumber);
        StringBuilder investmentNumber = new StringBuilder();

        return investmentNumber.append(year).append(randomNumber).toString();
    }
}
