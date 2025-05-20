package com.think.ms_demo.dto;

import java.util.List;

import com.think.ms_demo.external.Review;
import com.think.ms_demo.external.Vendor;

public class BookDTO {

    private Long bookid;
    private String title;
    private String genre;
    private String description;
    private String price;
    private boolean available;
    private Vendor vendor;
   // private List<Review> review;



    public BookDTO() {
    }


    // Constructor
    public BookDTO(Vendor vendor) {
        this.vendor = vendor;
    }


    public Long getBookid() {
        return bookid;
    }


    public void setBookid(Long bookid) {
        this.bookid = bookid;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getGenre() {
        return genre;
    }


    public void setGenre(String genre) {
        this.genre = genre;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getPrice() {
        return price;
    }


    public void setPrice(String price) {
        this.price = price;
    }


    public boolean isAvailable() {
        return available;
    }


    public void setAvailable(boolean available) {
        this.available = available;
    }


    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

   // public List<Review> getReview() {
    //    return review;
    //}
    //public void setReview(List<Review> reviews) {
      //  this.review = reviews;
    //}

}