package com.portoflio.api.controllers;

import com.portoflio.api.dto.AuthenticationRequestDTO;
import com.portoflio.api.services.JwtService;
import com.portoflio.api.services.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);
    private final JwtService jwtService;
    private final UsersService usersService;
    private final AuthenticationManager authenticationManager;

    public AuthController(JwtService jwtService, AuthenticationManager authenticationManager, UsersService usersService) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.usersService = usersService;
    }
    @PostMapping("/token")
    public String token(@RequestBody AuthenticationRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                //loginRequest.getUsername(),
                loginRequest.getEmail(),
                loginRequest.getPassword()
        ));
        String token = jwtService.generateToken(authentication);
        return token;
    }
}