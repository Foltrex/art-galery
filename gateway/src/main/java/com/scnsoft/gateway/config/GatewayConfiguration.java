package com.scnsoft.gateway.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDiscoveryClient
public class GatewayConfiguration {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("art-service", r -> r
                        .path("/art-service/**")
                        .filters(f -> f.rewritePath("/art-service/(?<segment>.*)", "/${segment}")
                                .setResponseHeader("Access-Control-Allow-Origin", "*")
                                .setResponseHeader("Access-Control-Allow-Methods", "*")
                                .setResponseHeader("Access-Control-Expose-Headers", "X-Total-Count"))
                        .uri("lb://ART-SERVICE"))
                .route("user-service", r -> r
                        .path("/user-service/**")
                        .filters(f -> f.rewritePath("/user-service/(?<segment>.*)", "/${segment}")
                                .setResponseHeader("Access-Control-Allow-Origin", "*")
                                .setResponseHeader("Access-Control-Allow-Methods", "*")
                                .setResponseHeader("Access-Control-Expose-Headers", "X-Total-Count"))
                        .uri("lb://USER-SERVICE"))
                .route("file-service", r -> r
                        .path("/file-service/**")
                        .filters(f -> f.rewritePath("/file-service/(?<segment>.*)", "/${segment}")
                                .setResponseHeader("Access-Control-Allow-Origin", "*")
                                .setResponseHeader("Access-Control-Allow-Methods", "*")
                                .setResponseHeader("Access-Control-Expose-Headers", "X-Total-Count"))
                        .uri("lb://FILE-SERVICE"))
                .build();
    }

}
