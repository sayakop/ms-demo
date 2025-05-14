package com.think.ms_demo.service;

import java.util.List;

import com.think.ms_demo.dto.BookWithVendorDTO;
import com.think.ms_demo.model.Book;

public interface BookService {

    public Book addBooks(Book book);
    public boolean deleteBook(Long bookid);
    public BookWithVendorDTO getBook(Long bookid);
    public List<BookWithVendorDTO> getAllBooks();
    public Book updateBooks(Long bookid,Book book);

}
