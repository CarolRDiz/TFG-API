package com.portoflio.api.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String date;
    private Float price;
    private List<String> image_ids;
    private String description;
    private List<String> tags;

    @JsonBackReference
    @ToString.Exclude
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<ProductCategory> productCategories = new HashSet<ProductCategory>();

    private Boolean visibility;
    public Product(Product product) {
        this.name =         product.getName();
        this.date =         product.getDate();
        this.price =        product.getPrice();
        this.description =  product.getDescription();
        this.tags =         product.getTags();
        this.productCategories =     product.getProductCategories();
        this.visibility =   product.getVisibility();
    }
}
