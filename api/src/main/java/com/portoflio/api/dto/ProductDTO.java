package com.portoflio.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private Float price;
    private List<String> image_ids;
    private String thumbnail_image_id;
    private String description;
    private List<String> tags;
    private Boolean visibility;
    private Set<Long> category_ids;
    private Integer sellCount;
}
