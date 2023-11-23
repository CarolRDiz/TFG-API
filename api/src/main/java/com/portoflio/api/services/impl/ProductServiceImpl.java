package com.portoflio.api.services.impl;

import com.portoflio.api.dao.CategoryRepository;
import com.portoflio.api.dao.ProductRepository;
import com.portoflio.api.dto.IllustrationCreateDTO;
import com.portoflio.api.dto.IllustrationDTO;
import com.portoflio.api.dto.ProductCreateDTO;
import com.portoflio.api.dto.ProductDTO;
import com.portoflio.api.exceptions.NotFoundException;
import com.portoflio.api.models.Category;
import com.portoflio.api.models.Illustration;
import com.portoflio.api.models.Image;
import com.portoflio.api.models.Product;
import com.portoflio.api.services.CategoryService;
import com.portoflio.api.services.ImageService;
import com.portoflio.api.services.ProductService;
import com.portoflio.api.spec.ProductSpec;
import org.bson.types.Binary;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository repository;
    @Autowired
    ImageService imageService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CategoryService categoryService;

    private ModelMapper mapper = new ModelMapper();
    TypeMap<Product, Product> propertyMapper = this.mapper.createTypeMap(Product.class, Product.class);

    @Override
    public ProductDTO create(ProductCreateDTO newProduct) throws IOException {
        System.out.println("CREATE PRODUCT METHOD");
        Product product = this.mapper.map(newProduct, Product.class);
        if(newProduct.getImages()!=null){
            MultipartFile[] images = newProduct.getImages();
            List<String> image_ids = new ArrayList<>();
            Arrays.asList(images).stream().forEach(file -> {
                try {
                    String image_id = imageService.addImage( newProduct.getName(), file);
                    image_ids.add(image_id);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            product.setImage_ids(image_ids);
        }
        if(newProduct.getCategory_id()!=null){
            Optional<Category> oCategory = categoryRepository.findById(newProduct.getCategory_id());
            if(oCategory.isPresent()){
                System.out.println("CATEGORY FOUNDED:");
                Category category = oCategory.get();
                System.out.println(category);

                product.setCategory(category);
                product = repository.save(product);
                System.out.println("PRODUCT SAVED");
                System.out.println(product);
                //Creamos la referencia al producto en la categoría
                categoryService.addProduct(category.getId(), product);
                ProductDTO dto = this.mapper.map(product, ProductDTO.class);
                return dto;

            }
        }
        repository.save(product);
        ProductDTO dto = this.mapper.map(product, ProductDTO.class);
        return dto;
    }
    @Override
    public ProductDTO duplicate(Long id) throws IOException {
        Optional<Product> oProduct = repository.findById(id);
        if (oProduct.isPresent()) {
            Product product = oProduct.get();
            Product newProduct = new Product(product);
            //Duplicar las imágenes
            if( product.getImage_ids()!=null){
                List<String> newImageIdsList = new ArrayList<>();
                for(String image_id : product.getImage_ids()) {
                    Image image = imageService.getImage(image_id);
                    Image imageDuplicated = new Image(image.getTitle(), image.getImage());
                    String newImageId = imageService.save(imageDuplicated);
                    newImageIdsList.add(newImageId);
                };
                newProduct.setImage_ids(newImageIdsList);
            }
            repository.save(newProduct);
            ProductDTO dto = this.mapper.map(newProduct, ProductDTO.class);


            return dto;

        } else {
            throw new NotFoundException("Product not found");
        }
    }
    @Override
    public ProductDTO addImage (Long id, MultipartFile newImage) throws IOException {
        Optional<Product> oProduct = repository.findById(id);
        if (oProduct.isPresent()) {
            Product product = oProduct.get();
            String image_id = imageService.addImage( product.getName(), newImage);
            List<String> ids_list = new ArrayList<>(product.getImage_ids());
            ids_list.add(image_id);
            product.setImage_ids(ids_list);
            repository.save(product);
            ProductDTO productDTO = this.mapper.map(product, ProductDTO.class);
            return productDTO;
        } else {
            throw new NotFoundException("Illustration not found");
        }
    };
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
