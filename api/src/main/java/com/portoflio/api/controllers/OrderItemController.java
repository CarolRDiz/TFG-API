package com.portoflio.api.controllers;

import com.portoflio.api.dao.OrderItemRepository;
import com.portoflio.api.dto.OrderCreateDTO;
import com.portoflio.api.dto.OrderDTO;
import com.portoflio.api.exceptions.NotFoundException;
import com.portoflio.api.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin(origins = "http://localhost:4200/", maxAge = 3600)
public class OrderItemController {

    @Autowired
    OrderItemRepository repository;


    @GetMapping("/orderItems/")
    public ResponseEntity<Object> index() {
        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }

}
