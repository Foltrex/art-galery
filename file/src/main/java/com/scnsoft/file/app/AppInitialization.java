package com.scnsoft.file.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scnsoft.file.dto.AccountDto;
import com.scnsoft.file.dto.AccountType;
import com.scnsoft.file.dto.MetadataDto;
import com.scnsoft.file.feignclient.AuthFeignClient;

import lombok.RequiredArgsConstructor;

@Service
@PropertySource("/application.yml")
@RequiredArgsConstructor
public class AppInitialization {
    private final AuthFeignClient authFeignClient;

    @Value("${app.props.user.system.username}") 
    private String username;

    @Value("${app.props.user.system.password}") 
    private String password;

    
    @EventListener(ContextRefreshedEvent.class)
    public void postContextInitialization() {
        initializeFileServiceSystemUser();
    }
    
    private void initializeFileServiceSystemUser() {
        AccountDto accountDto = AccountDto.builder()
            .firstName("Admin")
            .lastName("System")
            .email(username)
            .password(password)
            .failCount(0)
            .accountType(AccountType.SYSTEM)
            .build();
        
        authFeignClient.register(accountDto);
    }
}
