package org.lms.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponceDto {

    @JsonProperty("access_token")
    public String accessToken;
    @JsonProperty("refresh_token")
    public String refreshToken;
    @JsonProperty("expires_in")
    public int expiresIn;
}

