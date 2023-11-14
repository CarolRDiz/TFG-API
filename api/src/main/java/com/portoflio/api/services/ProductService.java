package com.portoflio.api.services;

import com.portoflio.api.dto.ProductCreateDTO;
import com.portoflio.api.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    ProductDTO create (ProductCreateDTO newProduct);
    void delete (Long id);
    void deleteList (List<Long> ids);
    List<ProductDTO> findAll();
    List<ProductDTO> findFilter(String name, Double price);
    ProductDTO findById(Long id);
}
