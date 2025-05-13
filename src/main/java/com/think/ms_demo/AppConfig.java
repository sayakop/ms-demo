package com.think.ms_demo;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {


    @Bean
    @LoadBalanced
    // This RestTemplate bean is load-balanced, meaning it can be used to call services registered with Eureka
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
