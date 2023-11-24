package com.portoflio.api.controllers;

import com.portoflio.api.dto.ProductCreateDTO;
import com.portoflio.api.exceptions.NotFoundException;
import com.portoflio.api.services.ProductCategoryService;
import com.portoflio.api.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@CrossOrigin(origins = "http://localhost:5173/", maxAge = 3600)
public class ProductCategoryController {
    @Autowired
    ProductCategoryService service;
    // GET ALL PRODUCTS
    @GetMapping("/productCategories/")
    public ResponseEntity<Object> index() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

}
