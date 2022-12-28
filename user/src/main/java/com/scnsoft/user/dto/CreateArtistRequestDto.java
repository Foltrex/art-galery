package com.scnsoft.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Builder
public class CreateArtistRequestDto {

    @NotNull
    private UUID accountId;
}
