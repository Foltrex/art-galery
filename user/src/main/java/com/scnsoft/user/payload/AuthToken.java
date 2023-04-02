package com.scnsoft.user.payload;

import com.scnsoft.user.dto.AccountDto;
import com.scnsoft.user.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthToken {

    private String token;
    private String type;
}
