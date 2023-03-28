package com.scnsoft.user.payload;


import com.scnsoft.user.dto.MetadataDto;
import com.scnsoft.user.entity.Metadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bouncycastle.asn1.cms.MetaData;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty
    private String accountType;

//    @NotEmpty
//    private String firstname;

//    private String lastname;

    @NotEmpty
    private List<MetadataDto> metadata;
}
