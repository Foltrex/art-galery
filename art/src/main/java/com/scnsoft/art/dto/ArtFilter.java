package com.scnsoft.art.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ArtFilter {
    private String artistName;
    private UUID artistId;
    private UUID cityId;
    private String searchText;

    private List<Long> artFormat;
    private List<Long> artSize;
    private List<Long> artStyle;
    private List<Long> artTopic;
    private List<Long> artType;
    private List<Long> price;
}
