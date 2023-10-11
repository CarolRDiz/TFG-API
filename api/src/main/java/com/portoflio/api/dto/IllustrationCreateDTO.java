package com.portoflio.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IllustrationCreateDTO {
    private String title;
    private String date;
    private MultipartFile image;
}
