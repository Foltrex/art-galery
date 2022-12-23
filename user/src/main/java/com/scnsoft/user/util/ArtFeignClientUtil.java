package com.scnsoft.user.util;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(value = "art-service", url = "http://localhost:8081/artists")
public interface ArtFeignClientUtil {

    @GetMapping(path = "/{artistId}")
    Object getArtistById(@PathVariable("artistId") UUID userId);


    @GetMapping(path = "/test")
    ResponseEntity<String> test();

}
