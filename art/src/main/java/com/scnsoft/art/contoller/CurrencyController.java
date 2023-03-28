package com.scnsoft.art.contoller;

import com.scnsoft.art.dto.CurrencyDto;
import com.scnsoft.art.facade.CurrencyServiceFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("currencies")
public class CurrencyController {
    private final CurrencyServiceFacade currencyServiceFacade;

    @GetMapping
    public ResponseEntity<List<CurrencyDto>> findAll() {
        return ResponseEntity.ok(currencyServiceFacade.findAll());
    }

    @PostMapping
    public ResponseEntity<CurrencyDto> save(CurrencyDto currencyDto) {
        log.info(currencyDto.toString());
        return ResponseEntity.ok(currencyServiceFacade.save(currencyDto));
    }
}
