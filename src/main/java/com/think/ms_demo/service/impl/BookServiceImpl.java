package com.think.ms_demo.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.think.ms_demo.repository.BookRepository;
import com.think.ms_demo.dto.BookWithVendorDTO;
import com.think.ms_demo.external.Vendor;
import com.think.ms_demo.model.Book;
import com.think.ms_demo.service.BookService;


@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Autowired
    RestTemplate restTemplate;
    // Constructor-based dependency injection

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<BookWithVendorDTO> getAllBooks()
    {

        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private BookWithVendorDTO convertToDto(Book book) {
        BookWithVendorDTO bookWithVendorDTO = new BookWithVendorDTO(book, null);
        // Assuming you have a method to fetch vendor details using the vendorId
        bookWithVendorDTO.setBook(book);
                // Fetch vendor details using RestTemplate
                // You can replace the URL with your actual vendor service URL
                // For example: http://localhost:8081/vendor/{vendorId}
        //RestTemplate restTemplate = new RestTemplate();

                Vendor vendor = restTemplate.getForObject("http://vendor-demo/vendor/" + book.getVendorId() + "?raw=true", Vendor.class);
                bookWithVendorDTO.setVendor(vendor);
        return bookWithVendorDTO;
    }

    @Override
    public Book addBooks(Book book)
    {
       return bookRepository.save(book);
    }

    @Override
    public BookWithVendorDTO getBook(Long bookid) {
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
