package com.scnsoft.art.contoller;

import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.scnsoft.art.dto.ProposalDto;
import com.scnsoft.art.facade.ArtInfoServiceFacade;
import com.scnsoft.art.facade.ProposalServiceFacade;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/proposals")
public class ProposalController {
    private static final String X_TOTAL_COUNT_HEADER = "X-Total-Count";

    private final ProposalServiceFacade proposalServiceFacade;
    private final ArtInfoServiceFacade artInfoServiceFacade;

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<Page<ProposalDto>> findAllByAccountId(@PathVariable UUID accountId,  Pageable pageable) {
        return ResponseEntity.ok(proposalServiceFacade.findAllByAccountId(accountId, pageable));   
    }

    @RequestMapping(value="/accounts/{accountId}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> countByAccountId(@PathVariable UUID accountId) {
        long proposalsAmount = proposalServiceFacade.countByAccountId(accountId);
        return ResponseEntity.ok()
            .header(X_TOTAL_COUNT_HEADER, Objects.toString(proposalsAmount))
            .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        return ResponseEntity.ok(proposalServiceFacade.deleteById(id));
    }

    @PostMapping
    public ResponseEntity<ProposalDto> save(@RequestBody ProposalDto proposalDto) {
        if (
            proposalDto.getArtistConfirmation() != null && proposalDto.getArtistConfirmation() && 
            proposalDto.getOrganizationConfirmation() != null && proposalDto.getOrganizationConfirmation()
        ) {
            artInfoServiceFacade.createFromProposal(proposalDto);
        }
        
        return ResponseEntity.ok(proposalServiceFacade.save(proposalDto));
    }
}
