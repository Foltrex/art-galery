package com.scnsoft.user.util;

import com.scnsoft.user.dto.ArtistDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(value = "artist-service", url = "http://localhost:8081/artists")
public interface ArtistFeignClientUtil {

    @GetMapping(path = "/{id}")
    ResponseEntity<ArtistDto> getArtistById(@PathVariable("id") UUID artistId);

    @PostMapping()
    ResponseEntity<ArtistDto> save(@RequestBody ArtistDto artistDto);

}
