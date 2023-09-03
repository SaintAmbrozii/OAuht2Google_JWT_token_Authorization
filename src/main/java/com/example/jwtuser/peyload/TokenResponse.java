package com.example.jwtuser.peyload;

import lombok.*;

@Data
public class TokenResponse {

    private String accessToken;
    private String tokenType = "Bearer";


    public TokenResponse(String accessToken) {
        this.accessToken=accessToken;
    }
}
