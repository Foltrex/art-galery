package com.scnsoft.user.util;


import com.scnsoft.art.entity.Artist;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(value = "art-service", url = "http://localhost:8081/artists")
public interface ArtistFeignClientUtil {

    @GetMapping(path = "/{artistId}")
    Artist getArtistById(@PathVariable("artistId") UUID userId);

    @GetMapping(path = "/test")
    ResponseEntity<String> test();

    @PostMapping()
    ResponseEntity<Artist> save(@RequestBody Artist artist);



}
