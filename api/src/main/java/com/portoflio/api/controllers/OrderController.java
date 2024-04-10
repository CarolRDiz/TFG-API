package com.portoflio.api.controllers;

import com.portoflio.api.dto.OrderCreateDTO;
import com.portoflio.api.dto.OrderDTO;
import com.portoflio.api.exceptions.NotFoundException;
import com.portoflio.api.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200/", maxAge = 3600)
public class OrderController {

    @Autowired
    OrderService service;

    @PostMapping(value = "/order")
    public ResponseEntity<Object> postOrder(@RequestBody OrderCreateDTO postOrderRequest) {
        try {
            OrderDTO orderResponse = service.postOrder(postOrderRequest);
            return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}
