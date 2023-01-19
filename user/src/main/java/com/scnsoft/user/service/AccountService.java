package com.scnsoft.user.service;

import com.scnsoft.user.dto.RegisterRepresentativeRequestDto;
import com.scnsoft.user.dto.RepresentativeDto;
import com.scnsoft.user.entity.Account;

public interface AccountService {

    RepresentativeDto registerRepresentative(RegisterRepresentativeRequestDto registerRepresentativeRequestDto);

    Account findByEmail(String email);
}
