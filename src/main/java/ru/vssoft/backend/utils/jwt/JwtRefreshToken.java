package ru.vssoft.backend.utils.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtRefreshToken extends JwtTokenImpl {
    public JwtRefreshToken(
            @Value("${security.jwt.refreshSecret:s6e5c4r3e2t1}") String secret,
            @Value("${security.jwt.refreshExpiration:3600000}") String expiration) {
        super(secret, expiration);
    }
}
