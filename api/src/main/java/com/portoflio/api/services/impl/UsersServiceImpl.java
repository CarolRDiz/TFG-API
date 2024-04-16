package com.portoflio.api.services.impl;
import com.portoflio.api.dao.UsersRepository;
import com.portoflio.api.dto.UserPrincipalDTO;
import com.portoflio.api.dto.UsersDTO;
import com.portoflio.api.models.Users;
import com.portoflio.api.services.UsersService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService {
    @Autowired
    UsersRepository usersRepository;
    private ModelMapper mapper = new ModelMapper();
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public String signUpUser (Users newUser){
        boolean userExists = usersRepository.findByUsername(newUser.getUsername()).isPresent();
        if (userExists){
            throw new IllegalStateException("Username already taken");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(newUser.getPassword());
        newUser.setPassword(encodedPassword);
        usersRepository.save(newUser);
        return "it works";
    }

    @Override
    public UsersDTO findById(Long id) {
        Optional<Users> user = usersRepository.findById(id);
        if(user.isPresent()) {
            UsersDTO userDTO = this.mapper.map(user, UsersDTO.class);
            return userDTO;
        } else {
            //throw new NotFoundException("User not found");
            return null;
        }
    }
    @Override
    public UserPrincipalDTO getPrincipal(String username){
        Optional<Users> user = usersRepository.findByUsername(username);
        UserPrincipalDTO userDTO = this.mapper.map(user.get(), UserPrincipalDTO.class);
        return userDTO;
    }
    @Override
    public void delete(Long id) {
        Optional<Users> user = usersRepository.findById(id);
        if(user.isPresent()) usersRepository.delete(user.get());
    }

}