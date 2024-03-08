package com.portoflio.api.dto;

import com.portoflio.api.models.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateDTO {
    private String name;
    //private String date;
    private Float price;
    private MultipartFile[] images;
    private int thumbnail_index;
    private String description;
    private List<String> tags;
    private Boolean visibility;
    private List<Long> category_ids;
}
