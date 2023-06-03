package com.scnsoft.art.facade;

import com.scnsoft.art.dto.AccountType;
import com.scnsoft.art.dto.MetadataEnum;
import com.scnsoft.art.dto.ProposalDto;
import com.scnsoft.art.dto.ProposalFilter;
import com.scnsoft.art.dto.mapper.ProposalMapper;
import com.scnsoft.art.entity.Account;
import com.scnsoft.art.entity.Currency;
import com.scnsoft.art.entity.Metadata;
import com.scnsoft.art.entity.Organization;
import com.scnsoft.art.entity.Proposal;
import com.scnsoft.art.security.SecurityUtil;
import com.scnsoft.art.service.CurrencyService;
import com.scnsoft.art.service.FacilityService;
import com.scnsoft.art.service.OrganizationService;
import com.scnsoft.art.service.impl.ProposalServiceImpl;
import com.scnsoft.art.service.user.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.scnsoft.art.app.AppInitialization.GEORGIAN_CURRENCY_VALUE;

@Component
@RequiredArgsConstructor
public class ProposalServiceFacade {
    private final ProposalServiceImpl proposalService;
    private final ProposalMapper proposalMapper;
    private final CurrencyService currencyService;
    private final AccountService accountService;
    private final OrganizationService organizationService;
    private final FacilityService facilityService;


    public ProposalDto save(ProposalDto proposalDto) {
        Proposal proposal = proposalMapper.mapToEntity(proposalDto);
        Account account = accountService.findById(SecurityUtil.getCurrentAccountId());
        if(AccountType.ARTIST.equals(account.getAccountType())) {
            proposal.setAccountId(account.getId());
        } else if(AccountType.REPRESENTATIVE.equals(account.getAccountType())) {
            Organization organization = organizationService.findById(
                    account.getMetadata()
                            .stream()
                            .filter(m -> MetadataEnum.ORGANIZATION_ID.getValue().equals(m.getMetadataId().getKey()))
                            .findFirst()
                            .map(Metadata::getValue)
                            .map(UUID::fromString)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current account not properly configured")));
            proposal.setOrganization(organization);
            account.getMetadata()
                    .stream()
                    .filter(m -> MetadataEnum.FACILITY_ID.getValue().equals(m.getMetadataId().getKey()))
                    .map(Metadata::getValue)
                    .map(UUID::fromString)
                    .findFirst()
                    .ifPresent((f) -> {
                        facilityService.findById(f).map((facility) -> {
                            proposal.setFacilities(Collections.singletonList(facility));
                            return f;
                        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Facility does not exists"));
                    });
            proposal.setAccountId(proposalDto.getArt().getArtistAccountId());
        }
        proposal.setStatus(Proposal.ProposalStatus.SENT);
        Currency currency = currencyService.findAll().stream()
                .filter(c -> c.getValue().equals(GEORGIAN_CURRENCY_VALUE))
                .findFirst()
                .orElse(null);
        proposal.setCurrency(currency);
        return proposalMapper.mapToDto(proposalService.save(proposal));
    }

    public Page<ProposalDto> findAll(ProposalFilter filter, Pageable pageable) {
        return proposalMapper.mapPageToDto(proposalService.findAll(filter, pageable));
    }

    public Map<String, Long> count(ProposalFilter filter) {
        return new HashMap<>(){{
            filter.setReceived(1);
            put("received", proposalService.count(filter));
            filter.setReceived(-1);
            put("sent", proposalService.count(filter));
            filter.setReceived(0);
            put("approved", proposalService.count(filter));

        }};
    }

    public void deleteById(UUID id) {
        proposalService.deleteById(id);
    }

    public Optional<ProposalDto> findById(UUID id) {
        return proposalService.findById(id).map(proposalMapper::mapToDto);
    }
}
