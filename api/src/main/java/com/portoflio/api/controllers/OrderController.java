package com.portoflio.api.controllers;

import com.portoflio.api.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200/", maxAge = 3600)
public class OrderController {
    @Autowired
    OrderService orderService;

}
