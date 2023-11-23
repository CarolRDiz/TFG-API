package com.portoflio.api.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    private Double price;
    private List<String> image_ids;
    private String description;
    private List<String> tags;

    @ToString.Exclude
    @JsonBackReference
    @ManyToOne
    @JoinColumn()
    private Category category;

    private Boolean visibility;
    public Product(Product product) {
        this.name =         product.getName();
        this.date =         product.getDate();
        this.price =        product.getPrice();
        this.description =  product.getDescription();
        this.tags =         product.getTags();
        this.category =     product.getCategory();
        this.visibility =   product.getVisibility();
    }
}
