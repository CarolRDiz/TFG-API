package com.portoflio.api.controllers;

import com.portoflio.api.dto.IllustrationCreateDTO;
import com.portoflio.api.dto.IllustrationDTO;
import com.portoflio.api.exceptions.NotFoundException;
import com.portoflio.api.models.Illustration;
import com.portoflio.api.services.IllustrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin(origins = "http://localhost:5173/", maxAge = 3600)
@RestController
public class IllustrationController {
    @Autowired
    IllustrationService service;


    // GET AN ILLUSTRATION
    @GetMapping("/illustrations/{id}/")
    public ResponseEntity<Object> show(@PathVariable("id")Long id) {
        try {
            return new ResponseEntity<>(service.findById(id),HttpStatus.OK);
        } catch (NotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET ALL ILLUSTRATIONS
    @GetMapping("/illustrations/")
    public ResponseEntity<Object> index() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    // GET PUBLIC ILLUSTRATIONS
    @GetMapping("/illustrations/public/")
    public ResponseEntity<Object> indexPublic() {
        return new ResponseEntity<>(service.findPublic(), HttpStatus.OK);
    }

    // POST AN ILLUSTRATION
    @RequestMapping(path = "/illustrations/", method = POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> create (@ModelAttribute IllustrationCreateDTO newIllustration) throws IOException {
        return new ResponseEntity<>(service.create(newIllustration),HttpStatus.CREATED);
    }

    // UPDATE IMAGE FROM ILLUSTRATION
    @RequestMapping(path = "/illustrations/image/{id}/", method = PATCH, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Object> updateImage(@PathVariable Long id, @RequestParam("image") MultipartFile image, Model model){
        try{
            return new ResponseEntity<>(service.updateImage(id, image),HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(path = "/illustrations/delete/image/{id}/", method = PATCH)
    public ResponseEntity<Object> deleteImage(@PathVariable Long id){
        try{
            return new ResponseEntity<>(service.deleteImage(id),HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE AN ILLUSTRATION
    @RequestMapping(path = "/illustrations/{id}/", method = PATCH)
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody Map<String, Object> fields){
        try {
            return new ResponseEntity<>(service.updateChapterByFields(id, fields), HttpStatus.OK);
        } catch (NotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE AN ILLUSTRATION
    @DeleteMapping("/illustrations/{id}/")
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
    // DELETE SOME ILLUSTRATIONS
    @DeleteMapping("/illustrations/")
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
