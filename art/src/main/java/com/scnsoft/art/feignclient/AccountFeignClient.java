package com.scnsoft.art.feignclient;

import com.scnsoft.art.dto.AccountDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(value = "user-service-accounts", url = "http://localhost:8083/accounts")
public interface AccountFeignClient {

    // @GetMapping
    // Page<AccountDto> getAccountByEmail(String email);


    @GetMapping("/{id}")
    AccountDto findById(@PathVariable("id") UUID id);
}
