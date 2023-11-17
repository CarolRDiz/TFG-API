package com.portoflio.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.Binary;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IllustrationDTO {
    private Long id;
    private String name;
    private String description;
    //private String date;
    //private Binary image;
    private String image_id;
    private List<String> tags;}
