package com.portoflio.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IllustrationCreateDTO {
    private String name;
    private String description;
    //private String date;
    private MultipartFile image;
    //private List<String> tags;
    private Boolean visibility;
}
