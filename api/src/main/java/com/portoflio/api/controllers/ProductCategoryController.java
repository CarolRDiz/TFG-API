package com.portoflio.api.controllers;

import com.portoflio.api.exceptions.NotFoundException;
import com.portoflio.api.services.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@CrossOrigin(origins = "http://localhost:4200/", maxAge = 3600)
public class ProductCategoryController {
    @Autowired
    ProductCategoryService service;
    // GET ALL PRODUCTS
    @GetMapping("/productCategories/")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Object> index() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    /*
    @DeleteMapping("/productCategories/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
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

     */
    @PostMapping("/productCategories/{product_id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Object> createList(@PathVariable Long product_id, @RequestParam("category_ids") List<Long> category_ids) {
        HttpStatus httpStatus;
        try {
            service.createList(product_id, category_ids);
            httpStatus = HttpStatus.CREATED;
        } catch (NotFoundException e) {
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(httpStatus);
    }
    @DeleteMapping("/productCategories/{product_id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Object> deleteList(@PathVariable Long product_id, @RequestParam("category_ids") List<Long> category_ids) {
        HttpStatus httpStatus;
        try {
            service.deleteSome(product_id, category_ids);
            httpStatus = HttpStatus.CREATED;
        } catch (NotFoundException e) {
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(httpStatus);
    }

}
