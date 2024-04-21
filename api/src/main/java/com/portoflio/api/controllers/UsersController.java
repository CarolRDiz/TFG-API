package com.portoflio.api.controllers;

import com.portoflio.api.dao.UsersRepository;
import com.portoflio.api.dto.UserPrincipalDTO;
import com.portoflio.api.dto.UsersDTO;
import com.portoflio.api.exceptions.NotFoundException;
import com.portoflio.api.models.Users;
import com.portoflio.api.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.PATCH;

@RestController
@CrossOrigin(origins = "http://localhost:4200/", maxAge = 3600)
public class UsersController {
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    UsersService usersService;

    @GetMapping("/users/")
    public ResponseEntity<Object> index() {
        return new ResponseEntity<>(usersRepository.findAll(), HttpStatus.OK);
    }


    @GetMapping("/users/{id}/")
    public ResponseEntity<UsersDTO> show(@PathVariable("id") Long id) {
        final UsersDTO usersDTO = usersService.findById(id);
        if (usersDTO ==  null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(usersDTO);
    }

    @GetMapping("/users/principal/")
    public ResponseEntity<UserPrincipalDTO> principal(Principal principal) {
        UserPrincipalDTO userDTO = usersService.getPrincipal(principal.getName());
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @DeleteMapping("/users/{id}/")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        Optional<Users> user = usersRepository.findById(id);
        if(user.isPresent()) {
            usersService.delete(id);
            return new ResponseEntity<>(user.isPresent(), HttpStatus.OK);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/users/{id}/")
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody Users users) {
        Optional<Users> oldUser = usersRepository.findById(id);
        if(oldUser.isPresent()) {
            //prisoner.setId(id);
            usersRepository.save(users);
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }
    // UPDATE AN ILLUSTRATION
    @RequestMapping(path = "/users/", method = PATCH)
    public ResponseEntity<Object> update(Principal principal, @RequestBody Map<String, Object> fields){
        try {
            String email = principal.getName();
            return new ResponseEntity<>(usersService.updateByFields(email, fields), HttpStatus.OK);
        } catch (NotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}