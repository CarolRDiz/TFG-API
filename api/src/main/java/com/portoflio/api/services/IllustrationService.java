package com.portoflio.api.services;

import com.portoflio.api.dto.IllustrationCreateDTO;
import com.portoflio.api.dto.IllustrationDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IllustrationService {
    IllustrationDTO create(IllustrationCreateDTO newIlustration) throws IOException;
    void delete (Long id);
    List<IllustrationDTO> findAll();
    IllustrationDTO findById(Long id);
    IllustrationDTO updateImage (Long id, MultipartFile image) throws IOException;
    IllustrationDTO updateChapterByFields(Long id, Map<String, Object> fields);
}
