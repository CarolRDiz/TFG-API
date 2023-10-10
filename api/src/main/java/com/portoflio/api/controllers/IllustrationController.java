package com.portoflio.api.controllers;

import com.portoflio.api.dto.IllustrationCreateDTO;
import com.portoflio.api.exceptions.NotFoundException;
import com.portoflio.api.services.IllustrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class IllustrationController {
    @Autowired
    IllustrationService service;

    // GET ALL ILLUSTRATIONS
    @GetMapping("/illustrations/")
    public ResponseEntity<Object> index() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }
    // POST A ILLUSTRATION
    @RequestMapping(path = "/illustrations/", method = POST)
    public ResponseEntity<Object> create (@RequestBody IllustrationCreateDTO newIllustration){
        return new ResponseEntity<>(service.create(newIllustration),HttpStatus.CREATED);
    }
    @DeleteMapping("/illustrations/")
    public ResponseEntity<Object> delete (Long id) {
        HttpStatus httpStatus;
        try {
            service.delete(id);
            httpStatus = HttpStatus.CREATED;
        } catch (NotFoundException e) {
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(httpStatus);
    }
}
