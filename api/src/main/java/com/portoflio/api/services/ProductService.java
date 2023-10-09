package com.portoflio.api.services;

import com.portoflio.api.dto.ProductCreateDTO;
import com.portoflio.api.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    ProductDTO create (ProductCreateDTO newProduct);
    void delete (Long id);
    List<ProductDTO> findAll();
    ProductDTO findById(Long id);
}
