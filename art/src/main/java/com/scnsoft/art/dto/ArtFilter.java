package com.scnsoft.art.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ArtFilter {
    private String artistName;
    private UUID artistId;
    private UUID cityId;
    private String searchText;
}
