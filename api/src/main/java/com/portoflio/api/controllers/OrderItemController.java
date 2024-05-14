package com.portoflio.api.controllers;

import com.portoflio.api.dao.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class OrderItemController {

    @Autowired
    OrderItemRepository repository;

    /*
    @GetMapping("/orderItems/")
    public ResponseEntity<Object> index() {
        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }

     */

}
