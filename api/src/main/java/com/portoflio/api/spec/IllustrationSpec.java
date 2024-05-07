package com.portoflio.api.spec;

import com.portoflio.api.models.Illustration;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class IllustrationSpec {
    public static Specification<Illustration> getSpec(String name, Boolean visibility) {
        return (((root, query, criteriaBuilder) -> {
            List<Predicate> predicate = new ArrayList<>();
            if (name!=null && !(name.isEmpty())){
                predicate.add(criteriaBuilder.like(root.get("name"),"%"+name+"%"));
            }
            if (visibility!=null){
                predicate.add(criteriaBuilder.equal(root.get("visibility"),visibility));
            }
            return criteriaBuilder.and(predicate.toArray(new Predicate[0]));
        }));
    }
}
