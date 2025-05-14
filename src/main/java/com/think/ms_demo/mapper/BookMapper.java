package com.think.ms_demo.mapper;

import java.util.List;

import com.think.ms_demo.dto.BookDTO;
import com.think.ms_demo.external.Review;
import com.think.ms_demo.external.Vendor;
import com.think.ms_demo.model.Book;

public class BookMapper {

    public static BookDTO mapToBookDTO(Book book, Vendor vendor, List<Review> reviews) {
        BookDTO bookDTO = new BookDTO(vendor);
        bookDTO.setBookid(book.getBookid());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setGenre(book.getGenre());
        bookDTO.setDescription(book.getDescription());
        bookDTO.setPrice(book.getPrice());
        bookDTO.setAvailable(book.isAvailable());
        bookDTO.setVendor(vendor);
        bookDTO.setReview(reviews);
        return bookDTO;
    }

}
