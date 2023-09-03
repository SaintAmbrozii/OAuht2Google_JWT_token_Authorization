package com.example.jwtuser.peyload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {

    private String email;

    private String password;

    private String name;

    private String address;
}
