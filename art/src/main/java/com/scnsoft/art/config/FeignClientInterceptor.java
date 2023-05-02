package com.scnsoft.art.config;

import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.scnsoft.art.feignclient.AuthFeignClient;
import com.scnsoft.art.payload.LoginRequest;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class FeignClientInterceptor implements RequestInterceptor {

    @Value("${app.props.user.system.username}")
    private String username;

    @Value("${app.props.user.system.password}")
    private String password;

    private final AuthFeignClient authFeignClient;

    @Override
    public void apply(RequestTemplate template) {
        var authToken = authFeignClient.login(
            LoginRequest.builder()
                .email(username)
                .password(password)
                .build()
        );
        
        template.header(
            HttpHeaders.AUTHORIZATION, 
            String.format("%s %s", authToken.getType(), authToken.getType())
        );
    }
    
}
