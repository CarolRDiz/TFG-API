package com.portoflio.api.controllers;

import com.portoflio.api.dto.RegistrationDTO;
import com.portoflio.api.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200/", maxAge = 3600)
public class RegistrationController {
    @Autowired
    RegistrationService registrationService;

    @PostMapping("/registration/")
    public ResponseEntity<Object> register(@RequestBody RegistrationDTO newUser) {
        try {
            registrationService.register(newUser);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}