package com.think.ms_demo.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;


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


    // Constructor-based dependency injection
    public BookServiceImpl(BookRepository bookRepository, VendorServiceClient vendorServiceClient,ReviewServiceClient reviewServiceClient) {
        this.bookRepository = bookRepository;
        this.vendorServiceClient = vendorServiceClient;
        this.reviewServiceClient = reviewServiceClient;
    }

   @Override
    public List<BookDTO> getAllBooks() {
    List<Book> books = bookRepository.findAll();

    if (books == null || books.isEmpty()) {
        return List.of(); // Never return null
    }

    return books.stream()
                .map(this::convertToDtoSafe)
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
        Vendor vendor = null;
        List<Review> reviews = List.of();
        try{
            if(book.getVendorId() != null) {
                vendor = vendorServiceClient.getVendor(book.getVendorId());
            }
        }catch (Exception e) {
            log.error("Failed to fetch vendor for Book ID {}: {}", book.getBookid(), e.getMessage(), e);
        }

        try{
            if(book.getBookid() != null) {
                reviews = reviewServiceClient.getReview(book.getBookid());
            }
        }catch (Exception e) {
            log.error("Failed to fetch reviews for Book ID {}: {}", book.getBookid(), e.getMessage(), e);
        }

        return BookMapper.mapToBookDTO(book, vendor, reviews);
    }

}

