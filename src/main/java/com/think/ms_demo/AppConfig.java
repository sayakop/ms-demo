package com.think.ms_demo;

import java.io.IOException;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {


    @Bean
    @LoadBalanced
    // This RestTemplate bean is load-balanced, meaning it can be used to call services registered with Eureka
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(@NonNull ClientHttpResponse response) throws IOException {
                // Implement your error detection logic here
                return response.getStatusCode().isError();
            }

            @Override
            public void handleError(@NonNull ClientHttpResponse response) throws IOException {
                // Handle the error response here
                // You can log the error or throw a custom exception
                System.out.println("Error occurred: " + response.getStatusCode());
            }
        });
        return restTemplate;
    }
}