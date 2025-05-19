package com.think.ms_demo.service;

import java.util.List;

import com.think.ms_demo.dto.BookDTO;
import com.think.ms_demo.model.Book;

public interface BookService {

    public Book addBooks(Book book);
    public boolean deleteBook(Long bookid);
    public BookDTO getBook(Long bookid,Long reviewId);
    public List<BookDTO> getAllBooks();
    public Book updateBooks(Long bookid,Book book);

}
