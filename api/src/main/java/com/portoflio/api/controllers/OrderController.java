package com.portoflio.api.controllers;

import com.portoflio.api.dto.OrderCreateDTO;
import com.portoflio.api.dto.OrderDTO;
import com.portoflio.api.exceptions.NotFoundException;
import com.portoflio.api.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
public class OrderController {

    @Autowired
    OrderService service;

    @PostMapping(value = "/order/")
    public ResponseEntity<Object> postOrderPrincipal(Principal principal, @RequestBody OrderCreateDTO postOrderRequest) {
        try {
            OrderDTO orderResponse = service.postOrder(principal.getName(), postOrderRequest);
            return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping(value = "/order/guest")
    public ResponseEntity<Object> postOrder(@RequestBody OrderCreateDTO postOrderRequest) {
        try {
            OrderDTO orderResponse = service.postOrderGuest(postOrderRequest);
            return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/order/")
    public ResponseEntity<Object> index() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<Object> findOrder(@PathVariable("id") Long id) {
        try {
            return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PatchMapping("/order/{id}")
    public ResponseEntity<Object> updateOrder(@PathVariable("id") Long id, @RequestBody Map<String, Object> fields) {
        try {
            service.update(id, fields);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PatchMapping("/order/list")
    public ResponseEntity<Object> updateOrder(@RequestParam("ids") List<Long> ids, @RequestBody Map<String, Object> fields) {
        try {
            service.updateOrdersStatus(ids, fields);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}