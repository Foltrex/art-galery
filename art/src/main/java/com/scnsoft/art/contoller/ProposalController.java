package com.scnsoft.art.contoller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.scnsoft.art.dto.AccountDto;
import com.scnsoft.art.dto.ProposalDto;
import com.scnsoft.art.dto.mapper.impl.ArtistMapper;
import com.scnsoft.art.dto.mapper.impl.OrganizationMapper;
import com.scnsoft.art.dto.mapper.impl.ProposalMapper;
import com.scnsoft.art.entity.Artist;
import com.scnsoft.art.entity.Organization;
import com.scnsoft.art.entity.Proposal;
import com.scnsoft.art.feignclient.AccountFeignClient;
import com.scnsoft.art.service.ArtistService;
import com.scnsoft.art.service.OrganizationService;
import com.scnsoft.art.service.ProposalService;

import feign.Response;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/proposal")
public class ProposalController {

    private final ProposalService proposalService;
    private final ArtistService artistService;
    private final OrganizationService organizationService;
    private final AccountFeignClient accountFeignClient;
    private final OrganizationMapper organizationMapper;
    private final ArtistMapper artistMapper;
    private final ProposalMapper proposalMapper;

    @PostMapping
    public ResponseEntity<ProposalDto> create(@RequestBody ProposalDto proposalDto, 
                                            Authentication authentication) {
        ResponseEntity<AccountDto> accountResponseEntity = accountFeignClient.getAccountByEmail(authentication.getName());
        AccountDto accountDto = accountResponseEntity.getBody();
        UUID accountId = accountDto.getId();
        
        Proposal proposal = proposalMapper.mapToEntity(proposalDto);
        Artist artist = null;
        Organization organization = null;
        if (artistService.existWithAccountId(accountId)) {
            artist = artistService.findByAccountId(accountId);
            organization = organizationMapper.mapToEntity(proposalDto.getOrganizationDto());
        } else {
            organization = organizationService.findByAccountId(accountId);
            artist = artistMapper.mapToEntity(proposalDto.getArtistDto());
        }
        
        proposal.setArtist(artist);
        proposal.setOrganization(organization);

        ProposalDto responseDto = proposalMapper.mapToDto(proposalService.save(proposal));
        return ResponseEntity.ok(responseDto);
    }
}
