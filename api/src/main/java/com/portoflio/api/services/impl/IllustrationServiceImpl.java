package com.portoflio.api.services.impl;

import com.portoflio.api.dao.IllustrationRepository;
import com.portoflio.api.dto.IllustrationCreateDTO;
import com.portoflio.api.dto.IllustrationDTO;
import com.portoflio.api.dto.ProductDTO;
import com.portoflio.api.exceptions.NotFoundException;
import com.portoflio.api.models.Illustration;
import com.portoflio.api.models.Product;
import com.portoflio.api.services.IllustrationService;
import com.portoflio.api.services.ImageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IllustrationServiceImpl implements IllustrationService {
    @Autowired
    IllustrationRepository repository;
    @Autowired
    ImageService imageService;
    private ModelMapper mapper = new ModelMapper();

    @Override
    public IllustrationDTO create (IllustrationCreateDTO newIlustration) throws IOException {
        String image_id = imageService.addImage( newIlustration.getTitle(), newIlustration.getImage());

        Illustration illustration = this.mapper.map(newIlustration, Illustration.class);
        illustration.setImage_id(image_id);
        repository.save(illustration);
        return this.mapper.map(illustration, IllustrationDTO.class);
    }
    @Override
    public void delete(Long id){
        Optional<Illustration> oIllustration = repository.findById(id);
        if (oIllustration.isPresent()) {
            repository.delete(oIllustration.get());
        } else {
            throw new NotFoundException("Illustration not found");
        }
    }
    @Override
    public List<IllustrationDTO> findAll() {
        List<Illustration> illustrations = repository.findAll();
        List<IllustrationDTO> dtos = illustrations
                .stream()
                .map(illustration -> mapper.map(illustration, IllustrationDTO.class))
                .collect(Collectors.toList());
        return dtos;
    }
    @Override
    public IllustrationDTO findById(Long id) {
        Optional<Illustration> oIllustration = repository.findById(id);
        if (oIllustration.isPresent()) {
            IllustrationDTO illustrationDTO = this.mapper.map(oIllustration.get(), IllustrationDTO.class);
            return illustrationDTO;
        } else {
            throw new NotFoundException("Illustration not found");
        }
    }
}