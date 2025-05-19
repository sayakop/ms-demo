package com.think.ms_demo.client;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.think.ms_demo.external.Review;

@Service
public class ReviewServiceClient {

     private final RestTemplate restTemplate;

    @Value("${review.service.base-url}")
    private String reviewServiceBaseUrl; // e.g., http://localhost:8083

    public ReviewServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Review getReviewById(Long reviewId, Long bookId) {
        String url = reviewServiceBaseUrl + "/reviews/" + reviewId + "/book/" + bookId;
        try {
            return restTemplate.getForObject(url, Review.class);
        } catch (RestClientException e) {
            // Log and handle exception gracefully
            return null;
        }
    }
}