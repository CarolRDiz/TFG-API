package com.portoflio.api.controllers;

import com.portoflio.api.dto.RegistrationDTO;
import com.portoflio.api.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {
    @Autowired
    RegistrationService registrationService;

    @PostMapping("/registration/")
    public ResponseEntity<Object> register(@RequestBody RegistrationDTO newUser) {
        return new ResponseEntity<>(
                registrationService.register(newUser),
                HttpStatus.OK);
    }
}