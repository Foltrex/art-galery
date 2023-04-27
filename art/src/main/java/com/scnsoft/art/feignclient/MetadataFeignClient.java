package com.scnsoft.art.feignclient;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scnsoft.art.dto.MetaData;

@Component
@FeignClient(value = "user-service-metadatas", url = "http://localhost:8080/user-service/metadatas")
public interface MetadataFeignClient {
  @GetMapping
  MetaData findByKeyAndAccountId(@RequestParam(name = "accountId") UUID accountId, @RequestParam(name = "key") String key);
}
