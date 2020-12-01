package ru.vssoft.backend.utils.jwt;

import io.jsonwebtoken.*;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.function.Function;

public abstract class JwtTokenImpl implements JwtToken {
    private final String secret;
    private final String expiration;

    public JwtTokenImpl(String secret, String expiration) {
        this.secret = secret;
        this.expiration = expiration;
    }

    @Override
    public String build(UserDetails userDetails) {
        if (!StringUtils.hasText(userDetails.getUsername()))
            throw new IllegalArgumentException("Cannot create JWT Token without username");

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .compressWith(CompressionCodecs.DEFLATE)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(expiration)))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    @Override
    public boolean validate(String token, UserDetails userDetails) {
        final String username = getUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    @Override
    public String getUsername(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    protected Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    protected Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    protected <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = getAllClaimsFromToken(token);
            return claimsResolver.apply(claims);
        } catch (SignatureException e) {
            throw new AuthorizationServiceException(e.getMessage());
        }
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts
                .parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}
