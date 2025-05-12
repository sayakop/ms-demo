package com.think.ms_demo.dto;

import com.think.ms_demo.external.Vendor;
import com.think.ms_demo.model.Book;

public class BookWithVendorDTO {

    private Book book;
    private Vendor vendor;

// Constructor
    public BookWithVendorDTO(Book book, Vendor vendor) {
        this.book = book;
        this.vendor = vendor;
    }

    // Getters and Setters
    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }
}