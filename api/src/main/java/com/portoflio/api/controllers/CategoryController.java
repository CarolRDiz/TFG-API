package com.portoflio.api.controllers;

import com.portoflio.api.dto.CategoryCreateDTO;
import com.portoflio.api.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class CategoryController {

    @Autowired
    CategoryService bo;

    //@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @RequestMapping(path = "/categories/", method = POST)
    public ResponseEntity<Object> create(@RequestBody CategoryCreateDTO newCategory){
        try {
            return new ResponseEntity<>(bo.create(newCategory), HttpStatus.CREATED);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/categories/")
    public ResponseEntity<Object> findAll() {
        return new ResponseEntity<>(bo.findAll(), HttpStatus.OK);
    }

}