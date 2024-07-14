package com.portoflio.api.services.impl;
import com.portoflio.api.dto.RegisterRequestDTO;
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
    public void register (RegisterRequestDTO newUser){
        //boolean isValidUsername = usernameValidator.test(newUser.getUsername());
        boolean isValidEmail = usernameValidator.test(newUser.getEmail());
        /*
        if(!isValidUsername){
            throw new IllegalStateException("username not valid");
        }

         */
        Users user = this.mapper.map(newUser, Users.class);
        usersService.signUpUser(
                user
        );
    }
}
