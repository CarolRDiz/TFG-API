package com.portoflio.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.Binary;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IllustrationDTO {
    private Long id;
    private String title;
    private String date;
    private Binary image;}
