package com.example.jwtuser.security.oauth;

import com.example.jwtuser.config.AppProperties;
import com.example.jwtuser.domain.AuthProvider;
import com.example.jwtuser.domain.Role;
import com.example.jwtuser.domain.User;
import com.example.jwtuser.exception.BadRequestException;
import com.example.jwtuser.exception.OAuth2AuthentificationProcessingException;
import com.example.jwtuser.repo.UserRepo;
import com.example.jwtuser.security.TokenProvider;
import com.example.jwtuser.utils.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.example.jwtuser.utils.CotstantUtil.*;

@Component
public class Oauth2AuthentificationSucessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(Oauth2AuthentificationSucessHandler.class);

    private final TokenProvider tokenProvider;
    private final AppProperties appProperties;
    private final UserRepo userRepository;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    public Oauth2AuthentificationSucessHandler(TokenProvider tokenProvider, AppProperties appProperties, UserRepo userRepository, HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository) {
        this.tokenProvider = tokenProvider;
        this.appProperties = appProperties;
        this.userRepository = userRepository;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            LOGGER.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) {
        User user;
        Optional<User> userIfExist;
        DefaultOidcUser defaultOidcUser = (DefaultOidcUser) authentication.getPrincipal();
        Map<String, Object> attributes = defaultOidcUser.getAttributes();

        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        OAuth2AuthorizationRequest oAuth2AuthorizationRequest = CookieUtil
                .getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(cookie -> CookieUtil.deserialize(cookie, OAuth2AuthorizationRequest.class)).orElse(null);

        if (oAuth2AuthorizationRequest == null) {
            throw new OAuth2AuthentificationProcessingException("OAuth authorization request is null");
        }

        Map<String, Object> authorizationAttributes = oAuth2AuthorizationRequest.getAttributes();
        String provider = (String) authorizationAttributes.get(REGISTRATION_ID);

        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException("Sorry! We've got an Unauthorized Redirect URI " +
                    "and can't proceed with the authentication");
        }

        if (attributes.isEmpty()) {
            throw new OAuth2AuthentificationProcessingException("Cannot find user data");
        }

        Oauth2userInfo oAuth2UserInfo = Oauth2UserInfoFactory.getOAuth2UserInfo(provider, attributes);
        userIfExist = userRepository.findByUsername(oAuth2UserInfo.getEmail());

        user = userIfExist.map(oAuth2User -> updateExistingUser(oAuth2User, oAuth2UserInfo))
                .orElseGet(() -> registerNewUser(oAuth2UserInfo, provider));

        String targetUrl = redirectUri.orElse(LOCALHOST_MAIN_PAGE);
        String token = tokenProvider.createToken(user);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam(TOKEN, token)
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // Only validate host and port. Let the clients use different paths if they want to
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                        return true;
                    }
                    return false;
                });
    }

    private User registerNewUser(Oauth2userInfo oAuthUser, String provider) {
        User user = new User();

        user.setEnabled(1);
        user.setAuthority(Collections.singleton(Role.ROLE_USER));
        user.setProvider(AuthProvider.valueOf(provider.toUpperCase()));
        user.setProviderId(oAuthUser.getId());
        user.setUsername(oAuthUser.getEmail());
        user.setFullName(oAuthUser.getName());
        user.setLocale(oAuthUser.getLocale());
        user.setGender(oAuthUser.getGender());
        user.setPassword(UUID.randomUUID().toString());
        user.setPicture(oAuthUser.getImageUrl());
        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, Oauth2userInfo oAuth2User) {
        existingUser.setFullName(oAuth2User.getName());
        existingUser.setLocale(oAuth2User.getLocale());
        existingUser.setPicture(oAuth2User.getImageUrl());
        return userRepository.save(existingUser);
    }
}
