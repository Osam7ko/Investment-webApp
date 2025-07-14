package com.osproject.microservices.userservice.dto;

import java.time.LocalDateTime;

public record AccountDto(Long id, String accountNumber, double balance, String accountType) {
}
