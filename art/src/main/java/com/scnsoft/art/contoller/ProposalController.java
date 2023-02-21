package com.scnsoft.art.contoller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scnsoft.art.dto.ProposalDto;
import com.scnsoft.art.facade.ProposalServiceFacade;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/proposals")
public class ProposalController {

    private final ProposalServiceFacade proposalServiceFacade;

    @PostMapping
    public ResponseEntity<ProposalDto> save(@RequestBody ProposalDto proposalDto) {
        return ResponseEntity.ok(proposalServiceFacade.save(proposalDto));
    }
}
