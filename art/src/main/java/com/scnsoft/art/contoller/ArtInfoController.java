package com.scnsoft.art.contoller;

import com.scnsoft.art.dto.ArtInfoDto;
import com.scnsoft.art.entity.Proposal;
import com.scnsoft.art.service.impl.ArtInfoServiceImpl;
import com.scnsoft.art.service.impl.ProposalServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/art-info")
@RequiredArgsConstructor
public class ArtInfoController {
    private final ArtInfoServiceImpl artInfoService;
    private final ProposalServiceImpl proposalService;

    @PostMapping
    public ResponseEntity<ArtInfoDto> create(@RequestBody ArtInfoDto artInfoDto) {
        Proposal proposal = proposalService.findById(artInfoDto.getProposalId());
        return Boolean.TRUE.equals(proposal.getArtistConfirmation())
                && Boolean.TRUE.equals(proposal.getOrganizationConfirmation())
                ? ResponseEntity.ok(artInfoService.create(artInfoDto))
                : ResponseEntity.badRequest().build();
    }
}
