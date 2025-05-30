package com.think.ms_demo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;

import com.think.ms_demo.repository.BookRepository;
import com.think.ms_demo.client.ReviewServiceClient;
//import com.think.ms_demo.client.ReviewServiceClient.ResponseWrapper;
import com.think.ms_demo.client.VendorServiceClient;
import com.think.ms_demo.dto.BookDTO;
import com.think.ms_demo.exception.BookException.NotFoundBookException;
import com.think.ms_demo.external.Review;
import com.think.ms_demo.external.ReviewResponse;
import com.think.ms_demo.external.Vendor;
import com.think.ms_demo.mapper.BookMapper;
import com.think.ms_demo.model.Book;
import com.think.ms_demo.service.BookService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class BookServiceImpl implements BookService {

    private static final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

    @Autowired
    private final BookRepository bookRepository;
    private VendorServiceClient vendorServiceClient;
    private ReviewServiceClient reviewServiceClient;
    //private RestTemplate restTemplate;


    // Constructor-based dependency injection
    public  BookServiceImpl(BookRepository bookRepository,
                           @Autowired(required = false) VendorServiceClient vendorServiceClient,
                           @Autowired(required = false) ReviewServiceClient reviewServiceClient) {
        this.bookRepository = bookRepository;
        //this.restTemplate = restTemplate;
        this.vendorServiceClient = vendorServiceClient;
        this.reviewServiceClient = reviewServiceClient;
    }


    private BookDTO convertToDto(Book book) {
        Vendor vendor = null;
        List<Review> reviews = new ArrayList<>();
        try{
            if(book.getVendorId() != null) {
                vendor = vendorServiceClient.getVendor(book.getVendorId());
                if (vendor == null) {
                    log.warn("Vendor not found for Book ID {}", book.getBookid());
                } else {
                    log.debug("Vendor fetched: {}", vendor);
                }
            }
        }catch (Exception e) {
            log.error("Failed to fetch vendor for Book ID {}: {}", book.getBookid(), e.getMessage(), e);
        }

        try{
            if(book.getBookid() != null) {
                ReviewResponse reviewResponse = reviewServiceClient.getReview(book.getBookid());
                if (reviewResponse != null && reviewResponse.getData() != null) {
                    reviews = reviewResponse.getData();
                    log.debug("Reviews fetched: {}", reviewResponse.getData());
                } else {
                    log.warn("No reviews found for Book ID {}", book.getBookid());
                }
            }
        }catch (Exception e) {
            log.error("Failed to fetch reviews for Book ID {}: {}", book.getBookid(), e.getMessage(), e);
        }

        BookDTO bookDTO = BookMapper.mapToBookDTO(book, vendor);
        bookDTO.setReview(reviews);
        return bookDTO;
    }

       private BookDTO convertToDtoSafe(Book book) {
        try {
            return convertToDto(book);
        } catch (Exception e) {
            log.error("Failed to convert Book ID {} to DTO: {}", book != null ? book.getBookid() : "null", e.getMessage(), e);
            return null;
        }
    }



   @Override
    public List<BookDTO> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        List<BookDTO> response = new ArrayList<>();
        for (Book book : books) {
            BookDTO bookDTO = new BookDTO();
            BeanUtils.copyProperties(book, bookDTO);
            if (book.getVendorId() != null) {
                try {
                    Vendor vendor = vendorServiceClient.getVendor(book.getVendorId());
                    bookDTO.setVendor(vendor);
                } catch (Exception e) {
                    log.error("Failed to fetch vendor for Book ID {}: {}", book.getBookid(), e.getMessage(), e);
                }

                try {
                    List<Review> reviews = reviewServiceClient.getReview(book.getBookid()).getData();
                    if (reviews != null && !reviews.isEmpty()) {
                        bookDTO.setReview(reviews);
                    }else
                        {
                            log.warn("No reviews found for Book ID {}", book.getBookid());
                        }
                    }catch (Exception e) {
                    log.error("Failed to fetch reviews for Book ID {}: {}", book.getBookid(), e.getMessage(), e);
                }
            }
            response.add(bookDTO);
            }

            return response;
        }


    @Override
    public Book addBooks(Book book)
    {
       return bookRepository.save(book);
    }

    @Override
    public BookDTO getBook(Long bookid) {
        Optional<Book> book = bookRepository.findById(bookid);
        if (book.isPresent()) {
            BookDTO bookDTO = convertToDtoSafe(book.get());
            if (bookDTO != null) {
                return bookDTO;
            } else {
                throw new NotFoundBookException("Book not found with ID: " + bookid);
            }
        } else {
            throw new NotFoundBookException("Book not found with ID: " + bookid);
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

    
}

