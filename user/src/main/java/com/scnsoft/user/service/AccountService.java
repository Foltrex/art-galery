package com.scnsoft.user.service;

import com.scnsoft.user.dto.AccountDto;
import com.scnsoft.user.dto.AuthTokenDto;
import com.scnsoft.user.dto.LoginRequestDto;
import com.scnsoft.user.dto.RegisterRepresentativeRequestDto;
import com.scnsoft.user.dto.RegisterRequestDto;
import com.scnsoft.user.dto.RepresentativeDto;

public interface AccountService {

    AuthTokenDto register(RegisterRequestDto registerRequestDto);

    RepresentativeDto registerRepresentativeToOrganization(RegisterRepresentativeRequestDto registerRepresentativeRequestDto);

    AuthTokenDto login(LoginRequestDto loginRequestDto);

    AccountDto findByEmail(String email);
}
