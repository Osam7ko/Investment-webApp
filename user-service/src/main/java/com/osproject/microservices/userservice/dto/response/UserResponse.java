package com.osproject.microservices.userservice.dto.response;

import com.osproject.microservices.userservice.dto.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private String responseCode;
    private String responseMessage;

    private UserInfo userInfo;

}
