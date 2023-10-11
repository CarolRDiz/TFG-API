package com.portoflio.api.services;

import com.portoflio.api.dto.IllustrationCreateDTO;
import com.portoflio.api.dto.IllustrationDTO;

import java.io.IOException;
import java.util.List;

public interface IllustrationService {
    IllustrationDTO create(IllustrationCreateDTO newIlustration) throws IOException;
    void delete (Long id);
    List<IllustrationDTO> findAll();
    IllustrationDTO findById(Long id);
}
