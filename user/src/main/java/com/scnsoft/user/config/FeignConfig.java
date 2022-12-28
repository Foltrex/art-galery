package com.scnsoft.user.config;

import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Configuration
@Slf4j
public class FeignConfig {

    @Bean
    public RequestInterceptor requestTokenBearerInterceptor() {
        return requestTemplate -> {
            if (RequestContextHolder.getRequestAttributes() != null && RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes) {
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                String authorization = request.getHeader("Authorization");
                log.info(authorization);
                if (StringUtils.isNotBlank(authorization)) {
                    requestTemplate.header("Authorization", authorization);
                }
            }
        };
    }

    @LoadBalanced
    @Bean
    RestTemplate loadBalanced() {
        return new RestTemplate();
    }

}
