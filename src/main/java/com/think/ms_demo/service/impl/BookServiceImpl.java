package com.think.ms_demo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.think.ms_demo.repository.BookRepository;
import com.think.ms_demo.dto.BookDTO;
import com.think.ms_demo.external.Review;
import com.think.ms_demo.external.Vendor;
import com.think.ms_demo.mapper.BookMapper;
import com.think.ms_demo.model.Book;
import com.think.ms_demo.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class BookServiceImpl implements BookService {

    private static final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository bookRepository;

    @Autowired
    private RestTemplate restTemplate;
    // Constructor-based dependency injection
    public BookServiceImpl(BookRepository bookRepository, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<BookDTO> getAllBooks()
    {

        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(this::convertToDtoSafe)
                .filter(bookDTO -> bookDTO != null) // Filter out null DTO
                .collect(Collectors.toList());
    }

    @Override
    public Book addBooks(Book book)
    {
       return bookRepository.save(book);
    }

    @Override
    public BookDTO getBook(Long bookid) {
        Optional<Book> optionalBook = bookRepository.findById(bookid);
        if (optionalBook.isEmpty()) {
            log.warn("Book with ID {} not found", bookid);
            return null; // or throw custom BookNotFoundException
        }

        return convertToDtoSafe(optionalBook.get());
    }
        
    @Override
    public boolean deleteBook(Long bookid)
    {
        Optional<Book> book = bookRepository.findById(bookid);
        if (book.isPresent()) {
            bookRepository.delete(book.get());
            log.info("Book with ID {} deleted successfully", bookid);
            return true; // book was found and deleted
        } else {
            log.warn("Attempted to delete non-existent book with ID {}", bookid);
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

    private BookDTO convertToDtoSafe(Book book) {
        try {
            return convertToDto(book);
        } catch (Exception e) {
            log.error("Failed to convert Book ID {} to DTO: {}", book != null ? book.getBookid() : "null", e.getMessage(), e);
            return null;
        }
    }

     private BookDTO convertToDto(Book book) {
        Vendor vendor = null;
        List<Review> reviews = new ArrayList<>();

        try {
            ResponseEntity<Vendor> vendorResponse = restTemplate.exchange(
                    "http://vendor-demo/vendor/" + book.getVendorId() + "?raw=true",
                    HttpMethod.GET,
                    null,
                    Vendor.class
            );
            if (vendorResponse.getStatusCode().is2xxSuccessful()) {
                vendor = vendorResponse.getBody();
            } else {
                log.warn("Vendor not found or error for book ID {}: {}", book.getBookid(), vendorResponse.getStatusCode());
            }
        } catch (Exception ex) {
            log.error("Error while fetching vendor for book ID {}: {}", book.getBookid(), ex.getMessage());
        }

        try {
            ResponseEntity<List<Review>> reviewResponse = restTemplate.exchange(
                     "http://localhost:<port>/review/" + book.getVendorId() + "?raw=true",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Review>>() {}
            );
            if (reviewResponse.getStatusCode().is2xxSuccessful() && reviewResponse.getBody() != null) {
                reviews = reviewResponse.getBody();
            } else {
                log.warn("No reviews or error for book ID {}: {}", book.getBookid(), reviewResponse.getStatusCode());
            }
        } catch (Exception ex) {
            log.error("Error while fetching reviews for book ID {}: {}", book.getBookid(), ex.getMessage());
        }

        return BookMapper.mapToBookDTO(book, vendor, reviews);
    }
}

