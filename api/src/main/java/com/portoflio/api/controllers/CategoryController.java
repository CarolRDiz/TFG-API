package com.portoflio.api.controllers;

import com.portoflio.api.dto.CategoryCreateDTO;
import com.portoflio.api.exceptions.NotFoundException;
import com.portoflio.api.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin(origins = "http://localhost:5173/", maxAge = 3600)
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

    @DeleteMapping("/categories/{id}/")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        HttpStatus httpStatus;
        try {
            bo.delete(id);
            httpStatus = HttpStatus.CREATED;
        } catch (NotFoundException e) {
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(httpStatus);
    }

}