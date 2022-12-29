package com.scnsoft.user.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Slf4j
public class FeignConfig {

    // Test to include JWT token in feign client
//    @Bean
//    public RequestInterceptor requestTokenBearerInterceptor() {
//        return requestTemplate -> {
//            if (RequestContextHolder.getRequestAttributes() != null && RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes) {
//                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//                String authorization = request.getHeader("Authorization");
//                log.info(authorization);
//                if (StringUtils.isNotBlank(authorization)) {
//                    requestTemplate.header("Authorization", authorization);
//                }
//            }
//        };
//    }

    @LoadBalanced
    @Bean
    RestTemplate loadBalanced() {
        return new RestTemplate();
    }

}
