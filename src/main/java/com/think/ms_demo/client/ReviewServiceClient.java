package com.think.ms_demo.client;


import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.think.ms_demo.external.Review;

@FeignClient(name = "review-service", url = "${review.service.base-url}")
public interface ReviewServiceClient {
    
    @GetMapping("/review/{reviewId}")
    List<Review> getReview(@PathVariable Long reviewId);

}