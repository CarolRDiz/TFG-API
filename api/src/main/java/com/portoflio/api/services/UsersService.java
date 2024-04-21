package com.portoflio.api.services;

import com.portoflio.api.dto.UserPrincipalDTO;
import com.portoflio.api.dto.UsersDTO;
import com.portoflio.api.models.Users;

import java.io.Serializable;
import java.util.Map;

public interface UsersService  extends Serializable {
    UsersDTO findById(Long id);
    UserPrincipalDTO getPrincipal(String username);
    UserPrincipalDTO updateByFields (String email, Map<String, Object> fields);
    void delete(Long id);
    String signUpUser (Users newUser);
}