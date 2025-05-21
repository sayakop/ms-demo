package com.think.ms_demo.client;

import java.util.ArrayList;

import org.springframework.cloud.openfeign.FallbackFactory;

import com.think.ms_demo.external.ReviewResponse;


public class ReviewServiceClientFallbackFactory implements FallbackFactory<ReviewServiceClient> {

    @Override
    public ReviewServiceClient create(Throwable cause) {
        return new ReviewServiceClient() {

            @Override
            public ReviewResponse getAllReviews() {
                System.err.println("Fallback triggered due to: " + cause.getMessage());
                return new ReviewResponse("Fallback: Service unavailable", new ArrayList<>());
            }

            @Override
            public ReviewResponse getReview(Long bookid) {
                System.err.println("Fallback triggered due to: " + cause.getMessage());
                return new ReviewResponse("Fallback: No review found for bookId " + bookid, new ArrayList<>());
            }
        };
    }

}
