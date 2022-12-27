package com.scnsoft.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CreateArtistRequestDto {

    @NotNull
    private UUID accountId;
}
