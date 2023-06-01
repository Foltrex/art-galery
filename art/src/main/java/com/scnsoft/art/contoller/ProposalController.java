package com.scnsoft.art.contoller;

import com.scnsoft.art.dto.ProposalBook;
import com.scnsoft.art.dto.ProposalDto;
import com.scnsoft.art.dto.ProposalFilter;
import com.scnsoft.art.facade.ArtInfoServiceFacade;
import com.scnsoft.art.facade.ProposalServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/proposals")
@Transactional
public class ProposalController {
    private static final String X_TOTAL_COUNT_HEADER = "X-Total-Count";

    private final ProposalServiceFacade proposalServiceFacade;
    private final ArtInfoServiceFacade artInfoServiceFacade;

    @GetMapping("/{id}")
    public ResponseEntity<ProposalDto> findAllById(@PathVariable("id") UUID id) {
        return proposalServiceFacade.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/")
    public ResponseEntity<ProposalBook> findAll(ProposalFilter filter, Pageable pageable) {
        return ResponseEntity.ok(proposalServiceFacade.findAll(filter, pageable));
    }

    @RequestMapping(method = RequestMethod.HEAD)
    public ResponseEntity<Void> count(ProposalFilter filter) {
        long proposalsAmount = proposalServiceFacade.count(filter);
        return ResponseEntity.ok()
                .header(X_TOTAL_COUNT_HEADER, Objects.toString(proposalsAmount))
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        proposalServiceFacade.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProposalDto> update(@RequestBody ProposalDto proposalDto) {
        if (
                proposalDto.getArtistConfirmation() != null && proposalDto.getArtistConfirmation() &&
                        proposalDto.getOrganizationConfirmation() != null && proposalDto.getOrganizationConfirmation()
        ) {
            artInfoServiceFacade.createFromProposal(proposalDto);
        }
        return null;
    }

    @PostMapping
    public ResponseEntity<ProposalDto> save(@RequestBody ProposalDto proposalDto) {
        return ResponseEntity.ok(proposalServiceFacade.save(proposalDto));
    }
}
