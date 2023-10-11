package com.portoflio.api.controllers;

import com.portoflio.api.dto.ProductCreateDTO;
import com.portoflio.api.exceptions.NotFoundException;
import com.portoflio.api.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class ProductController {
    @Autowired
    ProductService service;

    // GET A PRODUCT
    @GetMapping("/products/{id}/")
    public ResponseEntity<Object> show(@PathVariable("id")Long id) {
        try {
            return new ResponseEntity<>(service.findById(id),HttpStatus.OK);
        } catch (NotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // GET ALL PRODUCTS
    @GetMapping("/products/")
    public ResponseEntity<Object> index() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }
    // POST A PRODUCT
    @RequestMapping(path = "/products/", method = POST)
    public ResponseEntity<Object> create(@RequestBody ProductCreateDTO newProduct){
        return new ResponseEntity<>(service.create(newProduct), HttpStatus.CREATED);
    }
    // DELETE A PRODUCT
    @DeleteMapping("/products/{id}/")
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