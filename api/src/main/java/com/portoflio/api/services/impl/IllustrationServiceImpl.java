package com.portoflio.api.services.impl;

import com.portoflio.api.dao.IllustrationRepository;
import com.portoflio.api.dto.IllustrationCreateDTO;
import com.portoflio.api.dto.IllustrationDTO;
import com.portoflio.api.exceptions.NotFoundException;
import com.portoflio.api.models.Illustration;
import com.portoflio.api.services.IllustrationService;
import com.portoflio.api.services.ImageService;
import org.bson.types.Binary;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IllustrationServiceImpl implements IllustrationService {
    @Autowired
    IllustrationRepository repository;
    @Autowired
    ImageService imageService;
    private ModelMapper mapper = new ModelMapper();
    TypeMap<Illustration, IllustrationDTO> propertyMapper = this.mapper.createTypeMap(Illustration.class, IllustrationDTO.class);

    @Override
    public IllustrationDTO create (IllustrationCreateDTO newIlustration) throws IOException {
        String image_id = imageService.addImage( newIlustration.getName(), newIlustration.getImage());
        Illustration illustration = this.mapper.map(newIlustration, Illustration.class);
        illustration.setImage_id(image_id);
        repository.save(illustration);
        IllustrationDTO dto = this.mapper.map(illustration, IllustrationDTO.class);
        Binary image = imageService.getImage(image_id).getImage();
        dto.setImage(image);
        return dto;
    }
    @Override
    public void delete(Long id){
        Optional<Illustration> oIllustration = repository.findById(id);
        if (oIllustration.isPresent()) {
            imageService.deleteImage(oIllustration.get().getImage_id());
            repository.delete(oIllustration.get());
        } else {
            throw new NotFoundException("Illustration not found");
        }
    }
    @Override
    public void deleteList(List<Long> ids){
        for(Long id : ids)
        {
            Optional<Illustration> oIllustration = repository.findById(id);
            if (oIllustration.isPresent()) {
                imageService.deleteImage(oIllustration.get().getImage_id());
                repository.delete(oIllustration.get());
            } else {
                throw new NotFoundException("Illustration not found");
            }
        }

    }
    @Override
    public List<IllustrationDTO> findAll() {
        Converter<String, Binary> binary = c -> imageService.getImage(c.getSource()).getImage();
        propertyMapper.addMappings(
                mapper -> mapper.using(binary).map(Illustration::getImage_id, IllustrationDTO::setImage)
        );
        List<Illustration> illustrations = repository.findAll();
        List<IllustrationDTO> dtos = illustrations
                .stream()
                .map(illustration -> mapper.map(illustration, IllustrationDTO.class))
                .collect(Collectors.toList());
        return dtos;
    }
    @Override
    public IllustrationDTO findById(Long id) {
        Converter<String, Binary> binary = c -> imageService.getImage(c.getSource()).getImage();
        propertyMapper.addMappings(
                mapper -> mapper.using(binary).map(Illustration::getImage_id, IllustrationDTO::setImage)
        );
        Optional<Illustration> oIllustration = repository.findById(id);
        if (oIllustration.isPresent()) {
            IllustrationDTO illustrationDTO = this.mapper.map(oIllustration.get(), IllustrationDTO.class);
            return illustrationDTO;
        } else {
            throw new NotFoundException("Illustration not found");
        }
    }
    @Override
    public IllustrationDTO updateImage (Long id, MultipartFile newImage) throws IOException {
        Converter<String, Binary> binary = c -> imageService.getImage(c.getSource()).getImage();
        propertyMapper.addMappings(
                mapper -> mapper.using(binary).map(Illustration::getImage_id, IllustrationDTO::setImage)
        );

        Optional<Illustration> oIllustration = repository.findById(id);
        if (oIllustration.isPresent()) {
            Illustration illustration = oIllustration.get();
            imageService.deleteImage(illustration.getImage_id());
            String image_id = imageService.addImage( illustration.getName(), newImage);
            illustration.setImage_id(image_id);
            repository.save(illustration);
            IllustrationDTO illustrationDTO = this.mapper.map(illustration, IllustrationDTO.class);
            return illustrationDTO;
        } else {
            throw new NotFoundException("Illustration not found");
        }
    };
    @Override
    public IllustrationDTO updateChapterByFields(Long id, Map<String, Object> fields){
        Converter<String, Binary> binary = c -> imageService.getImage(c.getSource()).getImage();
        propertyMapper.addMappings(
                mapper -> mapper.using(binary).map(Illustration::getImage_id, IllustrationDTO::setImage)
        );
        Optional<Illustration> illustration = repository.findById(id);
        if(illustration.isPresent()){
            fields.forEach((key,value) -> {
                Field field = ReflectionUtils.findField(Illustration.class, key);
                field.setAccessible(true);
                ReflectionUtils.setField(field, illustration.get(), value);
            });
            repository.save(illustration.get());
            return this.mapper.map(illustration.get(), IllustrationDTO.class);
        }
        else{
            throw new NotFoundException("Illustration not found");
        }
    }
}