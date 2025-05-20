package com.think.ms_demo.client;


import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.think.ms_demo.external.Review;

@FeignClient(name = "review-service", url = "${review.service.base-url}")
public interface ReviewServiceClient {
    
    @GetMapping("/review")
    ResponseWrapper getAllReviews();  // Wrap response to handle "data" field

    @GetMapping("/review/book/{bookid}")
    List<Review> getReview(@PathVariable("bookid") Long bookid);

    class ResponseWrapper {
        private List<Review> data;
        private String httpStatus;
        private String message;

        // getters and setters
        public List<Review> getData() { return data; }
        public void setData(List<Review> data) { this.data = data; }
        public String getHttpStatus() { return httpStatus; }
        public void setHttpStatus(String httpStatus) { this.httpStatus = httpStatus; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

}
}