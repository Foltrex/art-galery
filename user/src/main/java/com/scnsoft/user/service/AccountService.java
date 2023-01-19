package com.scnsoft.user.service;

import com.scnsoft.user.dto.RepresentativeDto;
import com.scnsoft.user.entity.Account;
import com.scnsoft.user.payload.RegisterRepresentativeRequest;

public interface AccountService {

    RepresentativeDto registerRepresentative(RegisterRepresentativeRequest registerRepresentativeRequest);

    Account findByEmail(String email);
}
