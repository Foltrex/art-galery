package com.scnsoft.user.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthToken {

    private String token;
    private String type;
}
