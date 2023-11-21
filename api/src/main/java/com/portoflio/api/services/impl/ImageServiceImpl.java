package com.portoflio.api.services.impl;


import com.portoflio.api.dao.ImageRepository;
import com.portoflio.api.models.Image;
import com.portoflio.api.services.ImageService;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageRepository imageRepo;

    @Override
    public String addImage(String title, MultipartFile file) throws IOException {
        Image image = new Image(title);
        image.setImage(
                new Binary(BsonBinarySubType.BINARY, file.getBytes()));
        image = imageRepo.insert(image);
        return image.getId();
    }
    @Override
    public String save(Image image) {
        image = imageRepo.insert(image);
        return image.getId();
    }
    @Override
    public Image getImage(String id) {
        return imageRepo.findById(id).get();
    }

    @Override
    public void deleteImage(String id){
        Optional<Image> image = imageRepo.findById(id);
        if (image.isPresent()){
            imageRepo.delete(image.get());
        }
    }
}
