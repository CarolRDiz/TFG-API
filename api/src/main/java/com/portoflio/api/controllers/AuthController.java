package com.portoflio.api.controllers;

import com.portoflio.api.dto.LoginRequestDTO;
import com.portoflio.api.services.TokenService;
import com.portoflio.api.services.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);
    private final TokenService tokenService;
    private final UsersService usersService;
    private final AuthenticationManager authenticationManager;

    public AuthController(TokenService tokenService, AuthenticationManager authenticationManager, UsersService usersService) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.usersService = usersService;
    }
    @PostMapping("/token")
    public String token(@RequestBody LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                //loginRequest.getUsername(),
                loginRequest.getEmail(),
                loginRequest.getPassword()
        ));
        String token = tokenService.generateToken(authentication);
        return token;
    }
}