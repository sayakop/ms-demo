package com.think.ms_demo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.think.ms_demo.repository.BookRepository;
import com.think.ms_demo.dto.BookWithVendorDTO;
import com.think.ms_demo.exception.BookException.NotFoundBookException;
import com.think.ms_demo.external.Vendor;
import com.think.ms_demo.model.Book;
import com.think.ms_demo.service.BookService;


@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<BookWithVendorDTO> getAllBooks()
    {

        List<Book> books = bookRepository.findAll();
        List<BookWithVendorDTO> bookWithVendorDTOs = new ArrayList<>();
        for (Book book : books) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                Vendor vendor = restTemplate.getForObject("http://localhost:8081/vendor/" + book.getVendorId(), Vendor.class);
                if (vendor != null) {
                    BookWithVendorDTO bookWithVendorDTO = new BookWithVendorDTO(book, vendor);
                    bookWithVendorDTOs.add(bookWithVendorDTO);
                } else {
                    System.out.println("Vendor information is not available for book ID: " + book.getBookid());
                }
            } catch (Exception e) {
                System.err.println("Error fetching vendor for book ID " + book.getBookid() + ": " + e.getMessage());
            }
        }
        return bookWithVendorDTOs;
    }

    @Override
    public Book addBooks(Book book)
    {
       return bookRepository.save(book);
    }

    @Override
    public Book getBook(Long bookid) {
            return bookRepository.findById(bookid)
                    .orElseThrow(() -> new NotFoundBookException("Requested Book Not Found"));
        }
        
    @Override
    public boolean deleteBook(Long bookid)
    {
        Optional<Book> book = bookRepository.findById(bookid);
        if (book.isPresent()) {
            bookRepository.delete(book.get());
            System.out.println("Book Deleted Successfully");
            return true; // book was found and deleted
        } else {
            System.out.println("Book Not found");
            return false; // book was not found
        }
    }

    @Override
    public Book updateBooks(Long bookid,Book book) {
        Optional<Book> optionalBook = bookRepository.findById(bookid);

        if (optionalBook.isPresent()) {
            Book existingBook = optionalBook.get();
            existingBook.setTitle(book.getTitle());
            existingBook.setGenre(book.getGenre());
            existingBook.setDescription(book.getDescription());
            existingBook.setPrice(book.getPrice());
            existingBook.setAvailable(book.isAvailable());
            return bookRepository.save(existingBook);
        } else {
            return bookRepository.save(book);
        }
    }
}
