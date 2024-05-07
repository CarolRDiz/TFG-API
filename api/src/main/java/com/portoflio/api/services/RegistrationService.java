package com.portoflio.api.services;

import com.portoflio.api.dto.RegistrationDTO;
import java.io.Serializable;

public interface RegistrationService extends Serializable {
    void register (RegistrationDTO newUser);
}