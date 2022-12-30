package com.scnsoft.art.contoller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scnsoft.art.dto.AccountDto;
import com.scnsoft.art.dto.ProposalDto;
import com.scnsoft.art.feignclient.AccountFeignClient;
import com.scnsoft.art.service.ArtistService;

import feign.Response;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/proposal")
public class ProposalController {

    private final ArtistService artistService;
    private final AccountFeignClient accountFeignClient;

    @PostMapping
    public ResponseEntity<ProposalDto> create(@RequestBody ProposalDto proposalDto, 
                                            Authentication authentication) {
        ResponseEntity<AccountDto> accountResponseEntity = accountFeignClient.getAccountByEmail(authentication.getName());
        AccountDto accountDto = accountResponseEntity.getBody();
        // if ()
        return null;
    }
}
