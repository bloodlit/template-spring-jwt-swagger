package ru.vssoft.backend.payload.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class LoginResponse {
    private Long id;
    private String token;
    private String refreshToken;
    private String username;
    private List<String> roles;

    public LoginResponse(String token, String refreshToken, Long id, String username, List<String> roles) {
        this.id = id;
        this.token = token;
        this.refreshToken = refreshToken;
        this.roles = roles;
        this.username = username;
    }
}
