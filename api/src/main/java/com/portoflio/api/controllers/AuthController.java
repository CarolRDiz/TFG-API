package com.portoflio.api.controllers;

import com.portoflio.api.dto.LoginRequestDTO;
import com.portoflio.api.services.TokenService;
import com.portoflio.api.services.UsersService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@CrossOrigin(origins = "http://localhost:4200/", maxAge = 3600)
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
        System.out.println("AuthController: authenticationManager");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                //loginRequest.getUsername(),
                loginRequest.getEmail(),
                loginRequest.getPassword()
        ));
        LOG.debug("Token requested for user: '{}'", authentication.getName());
        System.out.println("---------authentication------------");
        System.out.println(authentication);
        System.out.println(authentication.getName());

        String token = tokenService.generateToken(authentication);
        LOG.debug("Token granted: {}", token);

        return token;

        /*
        MyUserPrincipal user = (MyUserPrincipal) authentication.getPrincipal();
        UserPrincipalDTO userDTO = this.mapper.map(user, UserPrincipalDTO.class);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.AUTHORIZATION,
                        token
                )
                .body(userDTO);

         */
    }


    /*
    @PostMapping("/token")
    public String token(Authentication authentication) {
        LOG.debug("Token requested for user: '{}'", authentication.getName());
        String token = tokenService.generateToken(authentication);
        LOG.debug("Token granted: {}", token);
        return token;
    }
     */
}