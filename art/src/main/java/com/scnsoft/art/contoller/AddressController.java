package com.scnsoft.art.contoller;

import com.scnsoft.art.dto.AddressDto;
import com.scnsoft.art.facade.AddressServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("addresses")
@RequiredArgsConstructor
@Transactional
public class AddressController {
    private final AddressServiceFacade addressServiceFacade;

    @GetMapping("/cities/{cityId}")
    public ResponseEntity<Page<AddressDto>> findAllByCityId(@PathVariable UUID cityId, Pageable pageable) {
        return ResponseEntity.ok(addressServiceFacade.findAllByCityId(cityId, pageable));
    }
}
