package com.portoflio.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateDTO {
    private String name;
    //private String date;
    private Double price;
    private MultipartFile image;

    private String description;
}
