package com.scnsoft.art.feignclient;

import com.scnsoft.art.dto.AccountDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "user-service", url = "http://localhost:8083/accounts")
public interface AccountFeignClient {

    @GetMapping(path = "/byEmail/{email}")
    ResponseEntity<AccountDto> getAccountByEmail(@PathVariable("email") String email);

}
