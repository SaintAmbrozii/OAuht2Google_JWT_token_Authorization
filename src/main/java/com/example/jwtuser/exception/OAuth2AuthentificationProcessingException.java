package com.example.jwtuser.exception;

import org.springframework.security.core.AuthenticationException;


public class OAuth2AuthentificationProcessingException extends AuthenticationException {

    public OAuth2AuthentificationProcessingException(String msg, Throwable t) {
        super(msg, t);
    }

    public OAuth2AuthentificationProcessingException(String msg) {
        super(msg);
    }
}
