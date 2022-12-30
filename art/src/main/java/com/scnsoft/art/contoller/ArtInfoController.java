package com.scnsoft.art.contoller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scnsoft.art.entity.ArtInfo;
import com.scnsoft.art.entity.Proposal;
import com.scnsoft.art.service.ArtInfoService;
import com.scnsoft.art.service.ProposalService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/art-info")
@RequiredArgsConstructor
public class ArtInfoController {
    private final ArtInfoService artInfoService;
    private final ProposalService proposalService;

    @PostMapping
    public ResponseEntity<ArtInfo> create(@RequestBody UUID proposalId) {
        Proposal proposal = proposalService.findById(proposalId);
        return Boolean.TRUE.equals(proposal.getArtistConfirmation())
            && Boolean.TRUE.equals(proposal.getOrganisationConfirmation())
                ? ResponseEntity.ok(artInfoService.create(proposal))
                : ResponseEntity.badRequest().build();
    }
}
