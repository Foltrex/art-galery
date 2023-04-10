package com.scnsoft.art.contoller;

import com.scnsoft.art.dto.CityDto;
import com.scnsoft.art.facade.CityServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("cities")
@RequiredArgsConstructor
@Transactional
public class CityController {

    private final CityServiceFacade cityServiceFacade;

    @GetMapping
    public ResponseEntity<Page<CityDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(cityServiceFacade.findAll(pageable));
    }

    @GetMapping("/list")
    public List<CityDto> findAll() {
        return cityServiceFacade.findAll();
    }

    @PostMapping
    public ResponseEntity<CityDto> save(@RequestBody CityDto cityDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cityServiceFacade.save(cityDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityDto> update(@PathVariable UUID id, @RequestBody CityDto cityDto) {
        return ResponseEntity.ok(cityServiceFacade.updateById(id, cityDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(cityServiceFacade.deleteById(id));
    }
}
