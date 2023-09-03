package com.example.jwtuser.utils;

public class CotstantUtil {

    public static final String LOCALHOST_MAIN_PAGE = "http://localhost:8080/";
    public static final String REGISTRATION_ID = "registration_id";
    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    public static final String PROVIDER_ID = "sub";
    public static final String PROVIDER_NAME = "name";
    public static final String PROVIDER_EMAIL = "email";
    public static final String PROVIDER_GENDER = "gender";
    public static final String PROVIDER_LOCALE = "locale";
    public static final String PROVIDER_PICTURE = "picture";
    public static final String ERROR = "error";
    public static final String SINGLE_SLASH = "/";
    public static final String TOKEN = "token";
    public static final String OAUTH2_AUTHORIZE_BASE_URI = "/oauth2/authorize";
    public static final String OAUTH2_CALLBACK_BASE_URI = "/oauth2/callback/*";
    public static final String AUTH_MATCHER = "/auth/**";
    public static final String OAUTH2_MATCHER = "/oauth2/**";
    public static final String BASE_URI_MATCHER = "/**";
    public static final String AUTHORIZATION_SECURITY_HEADER = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String AUTHORIZATION_USER_TOKEN = "user_token";

    public static final int AUTHORIZATION_COOKIE_EXPIRE_SECONDS = 7200;
    public static final int COOKIE_EXPIRE_SECONDS = 180;
}
