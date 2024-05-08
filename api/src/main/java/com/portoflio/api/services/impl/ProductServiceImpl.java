package com.portoflio.api.services.impl;

import com.portoflio.api.dao.CategoryRepository;
import com.portoflio.api.dao.ProductCategoryRepository;
import com.portoflio.api.dao.ProductRepository;
import com.portoflio.api.dto.ProductCreateDTO;
import com.portoflio.api.dto.ProductDTO;
import com.portoflio.api.exceptions.NotFoundException;
import com.portoflio.api.models.*;
import com.portoflio.api.services.CategoryService;
import com.portoflio.api.services.ImageService;
import com.portoflio.api.services.ProductCategoryService;
import com.portoflio.api.services.ProductService;
import com.portoflio.api.spec.ProductSpec;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import static java.lang.Float.parseFloat;

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
    @Autowired
    ProductCategoryService productCategoryService;
    @Autowired
    ProductCategoryRepository productCategoryRepository;


    private ModelMapper mapper = new ModelMapper();
    TypeMap<Product, ProductDTO> propertyMapper = this.mapper.createTypeMap(Product.class, ProductDTO.class);

    @Override
    public ProductDTO convertDTO(Product product) {
        Converter<Set<ProductCategory>, Set<Long>> categoryId = c -> c.getSource().stream().map(r -> r.getCategory().getId()).collect(Collectors.toSet());
        propertyMapper.addMappings(
                mapper -> mapper.using(categoryId).map(Product::getProductCategories, ProductDTO::setCategory_ids)
        );


        ProductDTO dto = this.mapper.map(product, ProductDTO.class);
        return dto;
    }
    @Override
    public ProductDTO create(ProductCreateDTO newProduct) throws IOException {
        System.out.println("CREATE PRODUCT METHOD");

        Product product = new Product(newProduct);
        //AÑADIR LAS IMÁGENES
        if(newProduct.getImages()!=null){
            List<MultipartFile> images = Arrays.asList(newProduct.getImages());
            List<String> image_ids = new ArrayList<>();
            for (int i = 0; i < images.size(); i++) {
               // String ranking = (i + 1) + ": " + images.get(i);
                try{
                    String image_id = imageService.addImage( newProduct.getName(), images.get(i));
                    image_ids.add(image_id);
                    if (i==newProduct.getThumbnail_index()){
                        product.setThumbnail_image_id(image_id);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            product.setImage_ids(image_ids);
        }
        product = repository.save(product);

        if(newProduct.getCategory_ids()!=null && newProduct.getCategory_ids().size()!=0){
            //POR CADA CATEGORIA
            for(Long category_id : newProduct.getCategory_ids()){
                Optional<Category> oCategory = categoryRepository.findById(category_id);
                if(oCategory.isPresent()){
                    Category category = oCategory.get();
                    productCategoryService.create(product, category);
                }
            }
        }
        System.out.println(product.getProductCategories());
        ProductDTO dto = this.convertDTO(product);
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
            newProduct = repository.save(newProduct);
            ProductDTO dto = this.convertDTO(newProduct);

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
            ProductDTO dto = this.convertDTO(product);
            return dto;
        } else {
            throw new NotFoundException("Illustration not found");
        }
    };
    @Override
    public ProductDTO deleteImage (Long id, String imageId) {
        Optional<Product> oProduct = repository.findById(id);
        if (oProduct.isPresent()) {
            Product product = oProduct.get();
            imageService.deleteImage(imageId);
            var images = product.getImage_ids();
            List<String> filteredImages;
            filteredImages = images.stream()
                    .filter(imgId -> !imgId.equals(imageId))
                    .collect(Collectors.toList());
            product.setImage_ids(filteredImages);
            System.out.println(product.getThumbnail_image_id()!=null && product.getThumbnail_image_id().equals(imageId));
            if(product.getThumbnail_image_id().equals(imageId)){
                product.setThumbnail_image_id("");
            }
            repository.save(product);
            ProductDTO dto = this.convertDTO(product);
            return dto;
        } else {
            throw new NotFoundException("Product not found");
        }
    };
    @Override
    public void delete(Long id){
        Optional<Product> oProduct = repository.findById(id);
        if (oProduct.isPresent()) {
            //TODO: borrar imagenes
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
                .map(product ->this.convertDTO(product))
                .collect(Collectors.toList());
        return dtos;
    }
    @Override
    public List<ProductDTO> findFilter(String name, String category_name){
        Specification<Product> specification = ProductSpec.getSpec(name, category_name);
        List<Product> products = repository.findAll(specification);
        List<ProductDTO> dtos = products
                .stream()
                .map(product -> this.convertDTO(product))
                .collect(Collectors.toList());
        return dtos;
    }
    @Override
    public ProductDTO findById(Long id) {
        System.out.println("findById");

        Optional<Product> oProduct = repository.findById(id);
        if (oProduct.isPresent()) {
            System.out.println("product is present");
            ProductDTO dto = this.convertDTO(oProduct.get());
            return dto;
        } else {
            throw new NotFoundException("Product not found");
        }
    }
    @Override
    public ProductDTO findByIdPublic(Long id) {
        Optional<Product> oProduct = repository.findByIdAndVisibilityTrue(id);
        if (oProduct.isPresent()) {
            System.out.println("product is present");
            ProductDTO dto = this.convertDTO(oProduct.get());
            return dto;
        } else {
            throw new NotFoundException("Product not found");
        }
    }
    @Override
    public List<ProductDTO> findList (List<Long> ids) {
        List<ProductDTO> dtos = new ArrayList<ProductDTO>();
        for(Long id : ids){
            Optional<Product> oProduct = repository.findById(id);
            if (oProduct.isPresent()) {
                ProductDTO dto = this.convertDTO(oProduct.get());
                dtos.add(dto);
            } else {
                throw new NotFoundException("Product not found");
            }
        }
        return dtos;
    }

    @Override
    public Product getById(Long id) {
        Optional<Product> oProduct = repository.findById(id);
        if (oProduct.isPresent()) {
            return oProduct.get();
        } else {
            throw new NotFoundException("Product not found");
        }
    }

    @Override
    public ProductDTO updateProductByFields(Long id, Map<String, Object> fields){
        System.out.println("updateProductByFields");
        Optional<Product> product = repository.findById(id);
        if(product.isPresent()){
            fields.forEach((key,value) -> {
                if (key.equals("category_ids")) {
                    System.out.println("category_ids");
                    List<Long> old_category_ids = product.get().getProductCategories().stream().map(relation -> relation.getCategory().getId()).collect(Collectors.toList());
                    if(old_category_ids.size()!=0){
                        System.out.println("BORRAR CATEGORIAS");
                        //productCategoryService.deleteSome(old_category_ids);
                        for(Long old_category : old_category_ids)
                        {
                            System.out.println(id);
                            Optional<ProductCategory> oProductCategory = productCategoryRepository.findById(old_category);
                            if (oProductCategory.isPresent()) {
                                System.out.println("RELACION ENCONTRADA");
                                System.out.println(oProductCategory.get().getId());
                                productCategoryRepository.delete(oProductCategory.get());
                                Optional<ProductCategory> oTest = productCategoryRepository.findById(id);
                                if (oTest.isPresent()){System.out.println("NO SE HA BORRADO");}
                            } else {
                                throw new NotFoundException("ProductCategory not found");
                            }
                        }
                    }
                    List<Long> category_ids = (List<Long>) value;
                    System.out.println("CATEGORIAS A AÑADIR");
                    System.out.println(category_ids);
                    for (int i = 0; i < category_ids.size(); i++) {
                        Long idLong = Long.parseLong(String.valueOf(category_ids.get(i)));
                        Optional<Category> oCategory = categoryRepository.findById(idLong);
                        if (oCategory.isPresent()) {
                            productCategoryService.create(product.get(), oCategory.get());
                        }
                    }
                }
                else if (key.equals("thumbnail_index")) {

                }
                else {
                    Field field = ReflectionUtils.findField(Product.class, key);
                    field.setAccessible(true);
                    if(key.equals("price") && value!=null){
                        /* Number price = (Number) value;
                        Float priceFloat = price.floatValue();
                        ReflectionUtils.setField(field, product.get(), priceFloat);*/
                        Number price = parseFloat((String) value);
                        ReflectionUtils.setField(field, product.get(), price);
                    } else {
                        ReflectionUtils.setField(field, product.get(), value);
                    }
                }
            });
            repository.save(product.get());
            ProductDTO dto = this.convertDTO(product.get());
            return dto;
        }
        else{
            throw new NotFoundException("Product not found");
        }
    }
}
