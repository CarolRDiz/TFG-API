package com.portoflio.api.services.impl;
import com.portoflio.api.dao.UsersRepository;
import com.portoflio.api.dto.IllustrationDTO;
import com.portoflio.api.dto.UserPrincipalDTO;
import com.portoflio.api.dto.UsersDTO;
import com.portoflio.api.exceptions.NotFoundException;
import com.portoflio.api.models.Illustration;
import com.portoflio.api.models.Users;
import com.portoflio.api.services.UsersService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService {
    @Autowired
    UsersRepository usersRepository;
    private ModelMapper mapper = new ModelMapper();
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public String signUpUser (Users newUser){
        //boolean userExists = usersRepository.findByUsername(newUser.getUsername()).isPresent();
        boolean userExists = usersRepository.findByEmail(newUser.getEmail()).isPresent();
        if (userExists){
            throw new IllegalStateException("Email already taken");
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
    public UserPrincipalDTO getPrincipal(String email){
        Optional<Users> oUser = usersRepository.findByEmail(email);
        if(oUser.isPresent()){
            UserPrincipalDTO userDTO = this.mapper.map(oUser.get(), UserPrincipalDTO.class);
            return userDTO;
        }
        else {
            throw new NotFoundException("User not found");
        }
    }
    @Override
    public void delete(Long id) {
        Optional<Users> user = usersRepository.findById(id);
        if(user.isPresent()) usersRepository.delete(user.get());
    }
    @Override
    public UserPrincipalDTO updateByFields (String email, Map<String, Object> fields){
        System.out.println("updateByFields");
        Optional<Users> oUser = usersRepository.findByEmail(email);
        if(oUser.isPresent()){
            System.out.println("user is present");
            fields.forEach((key,value) -> {
                Field field = ReflectionUtils.findField(Users.class, key);
                field.setAccessible(true);
                ReflectionUtils.setField(field, oUser.get(), value);
            });
            usersRepository.save(oUser.get());
            System.out.println("user saved");
            return this.mapper.map(oUser.get(), UserPrincipalDTO.class);
        }
        else{
            throw new NotFoundException("User not found");
        }
    }

}