package ru.vssoft.backend.utils.jwt;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import ru.vssoft.backend.model.User;
import ru.vssoft.backend.payload.response.LoginResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Getter
public class JwtTokenFactory {
    private final JwtAccessToken jwtAccessToken;
    private final JwtRefreshToken jwtRefreshToken;

    public JwtTokenFactory(JwtAccessToken jwtAccessToken, JwtRefreshToken jwtRefreshToken) {
        this.jwtAccessToken = jwtAccessToken;
        this.jwtRefreshToken = jwtRefreshToken;
    }

    public LoginResponse response(User user) {
        String token = jwtAccessToken.build(user);
        String refreshToken = jwtRefreshToken.build(user);

        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new LoginResponse(
                token,
                refreshToken,
                user.getId(),
                user.getUsername(),
                roles
        );
    }
}
