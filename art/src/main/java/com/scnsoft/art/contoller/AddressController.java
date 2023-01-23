package com.scnsoft.art.contoller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scnsoft.art.dto.AddressDto;
import com.scnsoft.art.facade.AddressServiceFacade;
import com.scnsoft.art.service.AddressService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressServiceFacade addressServiceFacade;

    @GetMapping("/city/{cityId}")
    public ResponseEntity<List<AddressDto>> findAllByCityId(@PathVariable UUID cityId) {
        return ResponseEntity.ok(addressServiceFacade.findAllByCityId(cityId));
    }
}
