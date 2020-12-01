package ru.vssoft.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.vssoft.backend.model.User;
import ru.vssoft.backend.payload.request.LoginRequest;
import ru.vssoft.backend.payload.response.LoginResponse;
import ru.vssoft.backend.service.UserService;
import ru.vssoft.backend.utils.jwt.JwtTokenFactory;

import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/user")
@Api(tags = {"User manager"})
public class UserController {

    private final JwtTokenFactory jwtToken;

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Autowired
    public UserController(JwtTokenFactory jwtToken, AuthenticationManager authenticationManager, UserService userService) {
        this.jwtToken = jwtToken;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Authentication user")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest login) throws Exception {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
            User user = (User) authentication.getPrincipal();

            return ResponseEntity.ok(jwtToken.response(user));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/refresh")
    @ApiOperation(value = "Refresh Jwt token")
    public ResponseEntity<LoginResponse> refreshToken(@RequestParam("token") @NotBlank String token) {

        String username = jwtToken.getJwtRefreshToken().getUsername(token);
        User user = (User) userService.loadUserByUsername(username);

        if (jwtToken.getJwtRefreshToken().validate(token, user)) {
            return ResponseEntity.ok(jwtToken.response(user));
        } else
            throw new AuthorizationServiceException("Expired or invalid JWT refresh token");
    }
}
