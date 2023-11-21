package com.portoflio.api.dto;

import com.portoflio.api.models.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.Binary;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    //private String date;
    private Double price;
    //private Binary image;
    private List<String> image_ids;
    private String description;
    private List<String> tags;
    private Boolean visibility;
    private Category category;
}
