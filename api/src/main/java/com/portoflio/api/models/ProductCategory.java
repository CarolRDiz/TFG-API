package com.portoflio.api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "productCategories")
public class ProductCategory {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn()
    private Product product;
    @ManyToOne
    @JoinColumn()
    private Category category;

    public ProductCategory(Product product, Category category) {
        this.product = product;
        this.category = category;
    }
}