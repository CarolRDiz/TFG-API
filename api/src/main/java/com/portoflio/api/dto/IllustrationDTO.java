package com.portoflio.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Boolean visibility;
    //private List<String> tags;
}
