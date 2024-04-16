package com.portoflio.api.services.impl;
import com.portoflio.api.dto.RegistrationDTO;
import com.portoflio.api.models.Users;
import com.portoflio.api.services.RegistrationService;
import com.portoflio.api.services.UsernameValidator;
import com.portoflio.api.services.UsersService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    @Autowired
    UsernameValidator usernameValidator;
    @Autowired
    private UsersService usersService;

    private ModelMapper mapper = new ModelMapper();
    @Override
    public String register (RegistrationDTO newUser){
        boolean isValidUsername = usernameValidator.test(newUser.getUsername());
        if(!isValidUsername){
            throw new IllegalStateException("username not valid");
        }
        Users user = this.mapper.map(newUser, Users.class);
        return usersService.signUpUser(
                user
        );
    }
}