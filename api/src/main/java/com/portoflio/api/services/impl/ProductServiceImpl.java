package com.portoflio.api.services.impl;

import com.portoflio.api.dao.ProductRepository;
import com.portoflio.api.dto.IllustrationCreateDTO;
import com.portoflio.api.dto.IllustrationDTO;
import com.portoflio.api.dto.ProductCreateDTO;
import com.portoflio.api.dto.ProductDTO;
import com.portoflio.api.exceptions.NotFoundException;
import com.portoflio.api.models.Illustration;
import com.portoflio.api.models.Product;
import com.portoflio.api.services.ImageService;
import com.portoflio.api.services.ProductService;
import com.portoflio.api.spec.ProductSpec;
import org.bson.types.Binary;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository repository;
    @Autowired
    ImageService imageService;
    private ModelMapper mapper = new ModelMapper();

    @Override
    public ProductDTO create(ProductCreateDTO newProduct) throws IOException {
        //List<MultipartFile> images = newProduct.getImages();
        String image_id = imageService.addImage( newProduct.getName(), newProduct.getImage());
        Product product = this.mapper.map(newProduct, Product.class);
        product.setImage_id(image_id);

        /*
        List<String> image_ids = new ArrayList<>();
        List<Binary> image_binarys = new ArrayList<>();

        for(MultipartFile image : images){
            String image_id = imageService.addImage( newProduct.getName(), image);
            image_ids.add(image_id);
            Binary image_binary = imageService.getImage(image_id).getImage();
            image_binarys.add(image_binary);
        }
        product.setImage_ids(image_ids);
         */
        repository.save(product);
        Binary image = imageService.getImage(image_id).getImage();
        ProductDTO dto = this.mapper.map(product, ProductDTO.class);
        //dto.setImage(image);
        //dto.setImages(image_binarys);
        return dto;
    }

    @Override
    public void delete(Long id){
        Optional<Product> oProduct = repository.findById(id);
        if (oProduct.isPresent()) {
            repository.delete(oProduct.get());
        } else {
            throw new NotFoundException("Product not found");
        }
    }
    @Override
    public void deleteList(List<Long> ids){
        for(Long id : ids){
            Optional<Product> oProduct = repository.findById(id);
            if (oProduct.isPresent()) {
                repository.delete(oProduct.get());
            } else {
                throw new NotFoundException("Product not found");
            }
        }
    }
    @Override
    public List<ProductDTO> findAll() {
        List<Product> products = repository.findAll();
        List<ProductDTO> dtos = products
                .stream()
                .map(product -> mapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
        return dtos;
    }
    @Override
    public List<ProductDTO> findFilter(String name, Double price){
        Specification<Product> specification = ProductSpec.getSpec(name, price);
        List<Product> products = repository.findAll(specification);
        List<ProductDTO> dtos = products
                .stream()
                .map(product -> mapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
        return dtos;
    }
    @Override
    public ProductDTO findById(Long id) {
        Optional<Product> oProduct = repository.findById(id);
        if (oProduct.isPresent()) {
            ProductDTO productDTO = this.mapper.map(oProduct.get(), ProductDTO.class);
            return productDTO;
        } else {
            throw new NotFoundException("Product not found");
        }
    }
}
