package com.portoflio.api.services;

import com.portoflio.api.dto.UserPrincipalDTO;
import com.portoflio.api.dto.UsersDTO;
import com.portoflio.api.models.Users;

import java.io.Serializable;

public interface UsersService  extends Serializable {
    UsersDTO findById(Long id);
    UserPrincipalDTO getPrincipal(String username);

    void delete(Long id);
    String signUpUser (Users newUser);
}