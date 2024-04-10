package com.portoflio.api.dto;

import com.portoflio.api.models.Category;
import com.portoflio.api.models.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.Binary;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    //private String date;
    private Float price;
    //private Binary image;
    private List<String> image_ids;
    private String thumbnail_id;
    private String description;
    private List<String> tags;
    private Boolean visibility;
    private Set<Long> category_ids;
    private Integer sellCount;
}
