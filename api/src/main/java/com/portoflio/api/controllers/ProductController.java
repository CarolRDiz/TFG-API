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
    @DeleteMapping("/products/")
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
