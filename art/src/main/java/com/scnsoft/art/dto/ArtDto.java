package com.scnsoft.art.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.scnsoft.art.entity.ArtSize;
import com.scnsoft.art.entity.ArtStyle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtDto {
    private UUID id;
    private String name;
    private String description;

    private List<ArtStyle> artStyles;
    private ArtSize artSize;

    private UUID artistAccountId;
    private Date dateCreation;
}
