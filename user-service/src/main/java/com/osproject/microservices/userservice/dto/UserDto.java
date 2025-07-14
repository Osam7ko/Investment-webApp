package com.osproject.microservices.userservice.dto;


public record UserDto(Long id, String userName, String password, String email) {
}
