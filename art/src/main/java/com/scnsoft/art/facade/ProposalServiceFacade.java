package com.scnsoft.art.facade;

import com.scnsoft.art.dto.AccountType;
import com.scnsoft.art.dto.ArtDto;
import com.scnsoft.art.dto.MetadataEnum;
import com.scnsoft.art.dto.ProposalDto;
import com.scnsoft.art.dto.ProposalFilter;
import com.scnsoft.art.dto.mapper.ProposalMapper;
import com.scnsoft.art.entity.Account;
import com.scnsoft.art.entity.Currency;
import com.scnsoft.art.entity.EntityFile;
import com.scnsoft.art.entity.Metadata;
import com.scnsoft.art.entity.Organization;
import com.scnsoft.art.entity.Proposal;
import com.scnsoft.art.security.SecurityUtil;
import com.scnsoft.art.service.CurrencyService;
import com.scnsoft.art.service.FacilityService;
import com.scnsoft.art.service.FileService;
import com.scnsoft.art.service.OrganizationService;
import com.scnsoft.art.service.impl.ProposalServiceImpl;
import com.scnsoft.art.service.user.AccountService;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    private final FileService fileService;
    private final EntityManager entityManager;


    public ProposalDto create(ProposalDto proposalDto) {
        Proposal proposal = proposalMapper.mapToEntity(proposalDto);
        Account account = accountService.findById(SecurityUtil.getCurrentAccountId());
        proposal.setAccountId(account.getId());
        proposal.setUpdateAccountId(account.getId());
        if (AccountType.REPRESENTATIVE.equals(account.getAccountType())) {
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
        var page = proposalMapper.mapPageToDto(proposalService.findAll(filter, pageable));

        Map<UUID, EntityFile> primary = fileService
                .findAllThumbs(page.stream()
                        .map(ProposalDto::getArt)
                        .filter(Objects::nonNull)
                        .map(ArtDto::getId)
                        .toList())
                .stream()
                .collect(Collectors.toMap(
                        EntityFile::getEntityId,
                        Function.identity(),
                        (v1, v2) -> v1
                ));
        page.stream()
                .map(ProposalDto::getArt)
                .filter(Objects::nonNull)
                .forEach(art -> {
                    var file = primary.get(art.getId());
                    if(file != null) {
                        art.setFiles(Collections.singletonList(file));
                    }
                });
        return page;
    }

    public Map<String, Long> count(ProposalFilter filter) {
        return new HashMap<>() {{
            filter.setReceived(1);
            filter.setStatus(new ArrayList<>(){{
                add(Proposal.ProposalStatus.SENT);
                add(Proposal.ProposalStatus.AWAIT);
            }});
            put("received", proposalService.count(filter));
            filter.setReceived(-1);
            put("sent", proposalService.count(filter));
            filter.setStatus(new ArrayList<>(){{
                add(Proposal.ProposalStatus.APPROVED);
            }});
            filter.setReceived(0);
            put("approved", proposalService.count(filter));
        }};
    }

    public void deleteById(UUID id) {
        proposalService.findById(id, false).ifPresent((p) -> {
            UUID currentId = SecurityUtil.getCurrentAccountId();
            if(currentId == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must be authorised to perform following action");
            }

            Account account = accountService.findById(currentId);
            boolean allowed = relatedArtist(account, p) || relatedRepresentative(account, p);
            if(allowed) {
                p.setStatus(Proposal.ProposalStatus.CANCELED);
                p.setUpdateAccountId(currentId);
                proposalService.save(p);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You have no privileges to perform following action");
            }
        });
    }

    public Optional<ProposalDto> findById(UUID id) {
        return proposalService.findById(id, true).map(p -> {
            var prop = proposalMapper.mapToDto(p);
            prop.getArt().setFiles(fileService.findAllByEntityId(prop.getArt().getId()));

            prop.setAudit(AuditReaderFactory.get( entityManager )
                    .createQuery()
                    .forRevisionsOfEntity( Proposal.class, true, true )
                    .add( AuditEntity.id().eq( prop.getId() ) )
                    .addProjection(AuditEntity.selectEntity(true))
                    .addOrder(AuditEntity.revisionNumber().asc())
                    .getResultList()
                    .stream()
                    .map(v -> {
                        Map<String, Object> out = new HashMap<>();
                        for(Map.Entry<String, Object> entry : ((Map<String, Object>)v).entrySet()) {
                            switch (entry.getKey()) {
                                case "originalId" -> {
                                    var originalId = (Map<String, Object>) entry.getValue();
                                    out.put("id", originalId.get("id"));
                                    var revision = (DefaultRevisionEntity) originalId.get("REVISION_ID");
                                    out.put("revisionId", revision.getId());
                                    out.put("revisionDate", revision.getRevisionDate().getTime());
                                }
                                default -> out.put(entry.getKey(), entry.getValue() != null ? entry.getValue() : null);
                            }

                        }
                        return out;
                    })
                    .toList());
            return prop;
        });
    }

    public ProposalDto update(ProposalDto proposalDto) {
        Proposal existing = proposalService.findById(proposalDto.getId(), false)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proposal not found"));
        UUID accountId = SecurityUtil.getCurrentAccountId();
        var account = accountService.findById(accountId);
        if(relatedArtist(account, existing)) {
            existing.setArtistConfirmation(proposalDto.getArtistConfirmation());
            if (Proposal.ProposalStatus.SENT.equals(proposalDto.getStatus())) {
                existing.setOrganizationConfirmation(false);
            }
        } else if(relatedRepresentative(account, existing)) {
            existing.setOrganizationConfirmation(proposalDto.getOrganizationConfirmation());
            if (Proposal.ProposalStatus.SENT.equals(proposalDto.getStatus())) {
                existing.setArtistConfirmation(false);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You have no privileges to perform following action");
        }

        existing.setUpdateAccountId(accountId);

        if(Proposal.ProposalStatus.APPROVED.equals(existing.getStatus()) ||
                Proposal.ProposalStatus.CANCELED.equals(existing.getStatus())) {
            existing.setExhibited(proposalDto.getExhibited());
            return proposalMapper.mapToDto(proposalService.save(existing));
        }

        if(Boolean.TRUE.equals(existing.getArtistConfirmation()) && Boolean.TRUE.equals(existing.getOrganizationConfirmation())) {
            existing.setStatus(Proposal.ProposalStatus.APPROVED);
            proposalService.discardAllProposalsForArtExceptPassed(existing);
        } else if(Proposal.ProposalStatus.SENT.equals(proposalDto.getStatus())) {
            existing.setStatus(Proposal.ProposalStatus.SENT);
            existing.setCommission(proposalDto.getCommission());
        }
        proposalService.save(existing);
        return proposalDto;
    }

    private boolean relatedArtist(Account account, Proposal proposal) {
        if(!AccountType.ARTIST.equals(account.getAccountType())) {
            return false;
        }
        boolean allowed = account.getId().equals(proposal.getAccountId());
        if(!allowed) {
            allowed = account.getId().equals(proposal.getArt().getArtistAccountId());
        }
        return allowed;
    }


    private boolean relatedRepresentative(Account account, Proposal p) {
        if(!AccountType.REPRESENTATIVE.equals(account.getAccountType())) {
            return false;
        }
        boolean allowed = account.getId().equals(p.getAccountId());

        if(account.getAccountType().equals(AccountType.REPRESENTATIVE)) {
            Metadata metadata = account.getMetadata().stream()
                    .filter(m -> MetadataEnum.ORGANIZATION_ID.getValue().equals(m.getMetadataId().getKey()))
                    .findAny()
                    .orElse(null);
            allowed = metadata != null && UUID.fromString(metadata.getValue()).equals(p.getOrganization().getId());
        }
        return allowed;
    }

}
