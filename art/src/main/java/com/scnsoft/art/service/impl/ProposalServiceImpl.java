package com.scnsoft.art.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.scnsoft.art.dto.ProposalFilter;
import com.scnsoft.art.entity.Account;
import com.scnsoft.art.entity.Facility;
import com.scnsoft.art.entity.Organization;
import com.scnsoft.art.service.user.AccountService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.scnsoft.art.entity.Art;
import com.scnsoft.art.entity.Proposal;
import com.scnsoft.art.repository.ProposalRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.criteria.Predicate;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProposalServiceImpl {

    private final ProposalRepository proposalRepository;

    public Optional<Proposal> findById(UUID proposalId) {
        return proposalRepository.findById(proposalId);
    }

    public Proposal save(Proposal proposal) {
        return proposalRepository.save(proposal);
    }

    public Page<Proposal> findAll(ProposalFilter filter, Pageable pageable) {
         return proposalRepository.findAll(prepareSpec(filter), pageable);
    }

    public long count(ProposalFilter filter) {
        return proposalRepository.count(prepareSpec(filter));
    }

    public void discardAllProposalsForArtExceptPassed(Art art, Proposal proposal) {
        List<Proposal> proposals = proposalRepository.findByArt(art);
        for (Proposal p : proposals) {
            if (!Objects.equals(proposal.getId(), p.getId())) {
                p.setArtistConfirmation(false);
                proposalRepository.save(p);
            }
        }
    }

    public void deleteById(UUID id) {
        proposalRepository.deleteById(id);
    }


    /**
     *
     * @param filter filter
     * @return props
     */
    private Specification<Proposal> prepareSpec(ProposalFilter filter) {
        return ((r, query, cb) -> {
            Predicate out = cb.and();
            if(filter.getAccountId() != null) {
                out = cb.and(out, cb.equal(r.get(Proposal.Fields.accountId), filter.getAccountId()));
                if(filter.getReceived() == 1) {
                    out = cb.and(out, cb.equal(r.get(Proposal.Fields.artistConfirmation), Boolean.FALSE));
                    out = cb.and(out, cb.equal(r.get(Proposal.Fields.organizationConfirmation), Boolean.TRUE));
                } else if(filter.getReceived() == -1) {
                    out = cb.and(out, cb.equal(r.get(Proposal.Fields.artistConfirmation), Boolean.TRUE));
                    out = cb.and(out, cb.equal(r.get(Proposal.Fields.organizationConfirmation), Boolean.FALSE));
                } else {
                    out = cb.and(out, cb.equal(r.get(Proposal.Fields.artistConfirmation), Boolean.TRUE));
                    out = cb.and(out, cb.equal(r.get(Proposal.Fields.organizationConfirmation), Boolean.TRUE));
                }
            }
            if(filter.getFacilityId() != null) {
                out = cb.and(out, cb.equal(
                        r.get(Proposal.Fields.facilities).get(Facility.Fields.id),
                        filter.getFacilityId()));
            }
            if(filter.getOrganizationId() != null) {
                out = cb.and(out, cb.equal(
                        r.get(Proposal.Fields.organization).get(Organization.Fields.id),
                        filter.getOrganizationId()));
                if(filter.getReceived() == 1) {
                    out = cb.and(out, cb.equal(r.get(Proposal.Fields.artistConfirmation), Boolean.TRUE));
                    out = cb.and(out, cb.equal(r.get(Proposal.Fields.organizationConfirmation), Boolean.FALSE));
                } else if(filter.getReceived() == -1) {
                    out = cb.and(out, cb.equal(r.get(Proposal.Fields.artistConfirmation), Boolean.FALSE));
                    out = cb.and(out, cb.equal(r.get(Proposal.Fields.organizationConfirmation), Boolean.TRUE));
                } else {
                    out = cb.and(out, cb.equal(r.get(Proposal.Fields.artistConfirmation), Boolean.TRUE));
                    out = cb.and(out, cb.equal(r.get(Proposal.Fields.organizationConfirmation), Boolean.TRUE));
                }
            }
            return out;
        });
    }
}
