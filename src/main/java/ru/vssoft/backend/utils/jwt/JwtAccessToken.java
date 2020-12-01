package ru.vssoft.backend.utils.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Service
public class JwtAccessToken extends JwtTokenImpl {
    private static final String BEARER_TITLE = "Bearer ";
    private static final String HEADER_TITLE = "Authorization";

    public JwtAccessToken(
            @Value("${security.jwt.secret:s1e2c3r4e5t6}") String secret,
            @Value("${security.jwt.expiration:900000}") String expiration) {
        super(secret, expiration);
    }

    public String parseJwt(HttpServletRequest httpServletRequest) {
        String headerAuth = httpServletRequest.getHeader(HEADER_TITLE);
        if (StringUtils.hasText(headerAuth))
            if (headerAuth.startsWith(BEARER_TITLE))
                return headerAuth.substring(BEARER_TITLE.length());
            else {
                throw new AuthenticationServiceException("Authorization header starts with \"" + BEARER_TITLE.trim() + "\"");
            }

        return "";
    }
}
