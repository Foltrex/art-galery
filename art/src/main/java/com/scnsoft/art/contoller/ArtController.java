package com.scnsoft.art.contoller;

import com.scnsoft.art.dto.AccountDto;
import com.scnsoft.art.dto.ArtDto;
import com.scnsoft.art.facade.ArtServiceFacade;
import com.scnsoft.art.feignclient.FileFeignClient;
import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("arts")
@RequiredArgsConstructor
@Transactional
public class ArtController {
    private final ArtServiceFacade artServiceFacade;
    private final FileFeignClient fileFeignClient;

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void deleteRelatedAccountData(AccountDto accountDto) {
        artServiceFacade.deleteByAccountId(accountDto.getId());
    }

    @GetMapping
    public Page<ArtDto> findAll(
            Pageable pageable,
            @RequestParam(defaultValue = "") String artistName,
            @RequestParam(defaultValue = "") String cityName,
            @RequestParam(defaultValue = "") String artNameAndDescription
    ) {
        return artServiceFacade.findAll(
                pageable,
                artistName,
                cityName,
                artNameAndDescription
        );
    }

    @PostMapping
    public ResponseEntity<ArtDto> save(@RequestBody ArtDto artDto) {
        return ResponseEntity.ok(artServiceFacade.save(artDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(artServiceFacade.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        // fileFeignClient.deleteByArtId(id);
        artServiceFacade.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // @DeleteMapping("/accounts/{id}")
    // public void deleteByAccountId(@PathVariable UUID id) {
    //     artServiceFacade.deleteByAccountId(id);
    // }
    

    @GetMapping("/artists/{artistId}")
    public Page<ArtDto> findAllByArtistId(@PathVariable UUID artistId, Pageable pageable) {
        return artServiceFacade.findAllByArtistId(artistId, pageable);
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<Page<ArtDto>> findAllByAccountIdAndSearchText(
            @PathVariable UUID accountId,
            Pageable pageable,
            @RequestParam(defaultValue = "") String searchText,
            @RequestParam(defaultValue = "") String searchFilter,
            @RequestParam(defaultValue = "art name") String searchOption
    ) {
        Page<ArtDto> artDtoPage = artServiceFacade.findAllByAccountId(
            accountId, pageable, searchText, searchFilter, searchOption
        );
        return ResponseEntity.ok(artDtoPage);
    }
}
