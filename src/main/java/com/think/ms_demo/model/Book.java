package com.think.ms_demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Book {

    @Id
    private Long bookid;
    private String title;
    private String genre;
    private String description;
    private String price;
    private boolean available;
    private Long vendorId;

    
    public Book() {
    }

    public Book(Long bookid, String title, String genre, String description, String price, boolean available,
            Long vendorId) {
        this.bookid = bookid;
        this.title = title;
        this.genre = genre;
        this.description = description;
        this.price = price;
        this.available = available;
        this.vendorId = vendorId;
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
    public Long getVendorId() {
        return vendorId;
    }
    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

}
