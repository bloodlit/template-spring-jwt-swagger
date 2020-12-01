package ru.vssoft.backend.utils.jwt;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtToken {
    String build(UserDetails userDetails);
    String getUsername(String token);
    boolean validate(String token, UserDetails userDetails);
}
