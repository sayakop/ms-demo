package com.think.ms_demo.external;

import java.util.ArrayList;
import java.util.List;

public class ReviewResponse {

    private String message;
    private String httpStatus;
    private List<Review> data;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ReviewResponse(String string, ArrayList arrayList) {
        this.message = string;
        this.httpStatus = "500";
        this.data = arrayList;
    }
    // Getters and setters
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getHttpStatus() {
        return httpStatus;
    }
    public void setHttpStatus(String httpStatus) {
        this.httpStatus = httpStatus;
    }
    public List<Review> getData() {
        return data;
    }
    public void setData(List<Review> data) {
        this.data = data;
    }

}
