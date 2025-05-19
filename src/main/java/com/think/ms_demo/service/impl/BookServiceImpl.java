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
import com.think.ms_demo.client.ReviewServiceClient;
import com.think.ms_demo.client.VendorServiceClient;
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

    private VendorServiceClient vendorServiceClient;
    private ReviewServiceClient reviewServiceClient;


    @Autowired
    private RestTemplate restTemplate;
    // Constructor-based dependency injection
    public BookServiceImpl(BookRepository bookRepository, RestTemplate restTemplate, VendorServiceClient vendorServiceClient,ReviewServiceClient reviewServiceClient) {
        this.restTemplate = restTemplate;
        this.bookRepository = bookRepository;
        this.vendorServiceClient = vendorServiceClient;
        this.reviewServiceClient = reviewServiceClient;
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
        Optional<Book> bookOptional = bookRepository.findById(bookid);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            return convertToDtoSafe(book);
        } else {
            log.warn("Book with ID {} not found", bookid);
            return null; // or throw an exception
        }
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
        Vendor vendor = vendorServiceClient.getVendor(book.getVendorId());
        List<Review> reviews = reviewServiceClient.getReview(book.getBookid());
        BookDTO bookDTO = BookMapper.mapToBookDTO(book, vendor, reviews);
        return bookDTO;
    }
}

