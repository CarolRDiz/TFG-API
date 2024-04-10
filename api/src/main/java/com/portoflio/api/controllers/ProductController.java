package com.portoflio.api.controllers;

import com.portoflio.api.dto.ProductCreateDTO;
import com.portoflio.api.exceptions.NotFoundException;
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
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@CrossOrigin(origins = "http://localhost:4200/", maxAge = 3600)
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

    // GET LIST PRODUCTS
    @GetMapping("/products/list")
    public ResponseEntity<Object> findList(@RequestParam(value = "ids", required = true) List<Long> ids) {
        return new ResponseEntity<>(service.findList(ids), HttpStatus.OK);
    }


    // GET FILTERED PRODUCTS
    @GetMapping("/products/filter")
    public ResponseEntity<Object> indexFilter(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "price", required = false) Double price) {
        return new ResponseEntity<>(service.findFilter(name,price), HttpStatus.OK);
    }
    // POST A PRODUCT
    @RequestMapping(path = "/products/", method = POST,  consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> create(@ModelAttribute ProductCreateDTO newProduct) throws IOException {
        try {
            return new ResponseEntity<>(service.create(newProduct), HttpStatus.CREATED);
        }
        catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // DUPLICATE A PRODUCT
    @RequestMapping(path = "/products/duplicate/{id}/", method = POST)
    public ResponseEntity<Object> duplicate(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(service.duplicate(id), HttpStatus.CREATED);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // UPDATE AN PRODUCT
    /*
    @RequestMapping(path = "/products/{id}/", method = PATCH)
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody Map<String, Object> fields){
        try {
            return new ResponseEntity<>(service.updateProductByFields(id, fields), HttpStatus.OK);
        } catch (NotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

    // UPDATE AN PRODUCT
    @RequestMapping(path = "/products/{id}/", method = PATCH)
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody Map<String, Object> fields){
        try {
            return new ResponseEntity<>(service.updateProductByFields(id, fields), HttpStatus.OK);
        } catch (NotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // UPDATE IMAGE FROM Product
    @RequestMapping(path = "/products/image/{id}/", method = PATCH, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Object> updateImage(@PathVariable Long id, @RequestParam("image") MultipartFile image, Model model){
        try{
            return new ResponseEntity<>(service.addImage(id, image),HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/products/delete/image/{id}/{imageId}", method = PATCH)
    public ResponseEntity<Object> deleteImage(@PathVariable Long id, @PathVariable String imageId){
        try{
            return new ResponseEntity<>(service.deleteImage(id, imageId),HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
    // DELETE SOME PRODUCTS
    @DeleteMapping("/products/")
    public ResponseEntity<Object> deleteList (@RequestParam("ids") List<Long> ids) {
        HttpStatus httpStatus;
        try {
            service.deleteList(ids);
            httpStatus = HttpStatus.CREATED;
        } catch (NotFoundException e) {
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(httpStatus);
    }
}
