package com.think.ms_demo.external;

public class Review {

    private long reviewId;
    private String title;
    private String description;
    private long rating;
    private long bookid;



    public long getReviewId() {
        return reviewId;
    }
    public void setReviewId(long reviewId) {
        this.reviewId = reviewId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public long getRating() {
        return rating;
    }
    public void setRating(long rating) {
        this.rating = rating;
    }
    public long getBookid() {
        return bookid;
    }
    public void setBookid(long bookid) {
        this.bookid = bookid;
    }
}
