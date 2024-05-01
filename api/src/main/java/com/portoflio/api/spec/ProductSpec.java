package com.portoflio.api.spec;

import com.portoflio.api.models.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpec {
    public static Specification<Product> getSpec(String name, Double price, Boolean visibility) {
        return (((root, query, criteriaBuilder) -> {
            List<Predicate> predicate = new ArrayList<>();

            if (name!=null && !(name.isEmpty())){
                predicate.add(criteriaBuilder.equal(root.get("name"),name));
            }

            if (price!=null && !(price.isNaN())){
                predicate.add(criteriaBuilder.equal(root.get("price"),price));
            }
            if (visibility!=null){
                predicate.add(criteriaBuilder.equal(root.get("visibility"),visibility));
            }
            return criteriaBuilder.and(predicate.toArray(new Predicate[0]));
        }));
    }
}
