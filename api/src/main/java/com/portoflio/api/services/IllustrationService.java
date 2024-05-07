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
    void deleteList (List<Long> ids);
    List<IllustrationDTO> findAll();
    List<IllustrationDTO> findPublic();
    IllustrationDTO findById(Long id);
    IllustrationDTO updateImage (Long id, MultipartFile image) throws IOException;
    IllustrationDTO deleteImage (Long id);
    List<IllustrationDTO> findFilter(String name, Boolean visibility);
    IllustrationDTO updateIllustrationByFields(Long id, Map<String, Object> fields);
}
