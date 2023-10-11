package com.portoflio.api.controllers;

import com.portoflio.api.dto.IllustrationCreateDTO;
import com.portoflio.api.exceptions.NotFoundException;
import com.portoflio.api.services.IllustrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class IllustrationController {
    @Autowired
    IllustrationService service;


    // GET A ILLUSTRATION
    @GetMapping("/illustrations/{id}/")
    public ResponseEntity<Object> show(@PathVariable("id")Long id) {
        try {
            return new ResponseEntity<>(service.findById(id),HttpStatus.OK);
        } catch (NotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET ALL ILLUSTRATIONS
    @GetMapping("/illustrations/")
    public ResponseEntity<Object> index() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    // POST A ILLUSTRATION
    @RequestMapping(path = "/illustrations/", method = POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> create (@ModelAttribute IllustrationCreateDTO newIllustration) throws IOException {
        return new ResponseEntity<>(service.create(newIllustration),HttpStatus.CREATED);
    }

    @DeleteMapping("/illustrations/{id}/")
    public ResponseEntity<Object> delete (@PathVariable("id") Long id) {
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
