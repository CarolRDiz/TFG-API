package com.portoflio.api.controllers;

import com.portoflio.api.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

//@CrossOrigin(origins = "http://localhost:5173/")
@RestController
public class ImageController {
    @Autowired
    ImageService imageService;

    @PostMapping("/images/add")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public String addPhoto(@RequestParam("title") String title,
                           @RequestParam("image") MultipartFile image)
            throws IOException {
        return imageService.addImage(title, image);
    }
    /*
    @GetMapping("/images/{id}")
    public ResponseEntity<Object> getPhoto(@PathVariable String id) {
        Image image = imageService.getImage(id);
        //return "<img src=\"data:image/jpeg;base64, " + Base64.getEncoder().encodeToString(image.getImage().getData()) + "\">";
        String base64 = "data:image/jpeg;base64, " + Base64.getEncoder().encodeToString(image.getImage().getData());
        ImageDTO imageDTO = new ImageDTO(base64);
        return new ResponseEntity<>(imageDTO, HttpStatus.OK);
    }

     */
    @GetMapping("/images/{id}")
    public ResponseEntity<Object> getPhoto(@PathVariable String id) {
        byte[] image = imageService.getImage(id).getImage().getData();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }
}
