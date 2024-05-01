package com.portoflio.api.services;

import com.portoflio.api.dto.IllustrationDTO;
import com.portoflio.api.dto.ProductCreateDTO;
import com.portoflio.api.dto.ProductDTO;
import com.portoflio.api.models.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ProductService {
    ProductDTO create (ProductCreateDTO newProduct) throws IOException;
    ProductDTO duplicate (Long id) throws IOException;
    void delete (Long id);
    void deleteList (List<Long> ids);
    ProductDTO deleteImage (Long id, String imageId);
    List<ProductDTO> findAll();
    ProductDTO updateProductByFields(Long id, Map<String, Object> fields);
    List<ProductDTO> findFilter(String name, Double price, Boolean visibility);
    ProductDTO findById(Long id);
    ProductDTO findByIdPublic(Long id);
    ProductDTO addImage (Long id, MultipartFile image) throws IOException;
    Product getById(Long id);
    ProductDTO convertDTO (Product product);
    List<ProductDTO> findList (List<Long> ids);
}
