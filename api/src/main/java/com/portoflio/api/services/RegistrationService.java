package com.portoflio.api.services;

import com.portoflio.api.dto.RegisterRequestDTO;
import java.io.Serializable;

public interface RegistrationService extends Serializable {
    void register (RegisterRequestDTO newUser);
}