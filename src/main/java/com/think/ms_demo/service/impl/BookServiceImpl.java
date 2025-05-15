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

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<BookDTO> getAllBooks()
    {

        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

   private BookDTO convertToDto(Book book) {
    Vendor vendor = null;
    List<Review> reviews = new ArrayList<>();

    try {
        vendor = restTemplate.getForObject("http://vendor-demo/vendor/" + book.getVendorId() + "?raw=true", Vendor.class);
        } catch (Exception e) {
            log.error("Error fetching vendor for ID {}: {}", book.getVendorId(), e.getMessage(), e);
    }        
    try {
        ResponseEntity<List<Review>> reviewResponse = restTemplate.exchange(
            "http://reviewms/review/" + book.getBookid(),
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Review>>() {});
        reviews = reviewResponse.getBody();
        } 
        catch (Exception e) 
        {
            log.error("Error fetching reviews for book ID {}: {}", book.getBookid(), e.getMessage(), e);
        }

        BookDTO bookDTO = BookMapper.mapToBookDTO(book, vendor, reviews);                 
        bookDTO.setVendor(vendor);
        return bookDTO;
}


    @Override
    public Book addBooks(Book book)
    {
       return bookRepository.save(book);
    }

    @Override
    public BookDTO getBook(Long bookid) {
        Book book = bookRepository.findById(bookid).orElse(null);
        return convertToDto(book);
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
