package com.think.ms_demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.think.ms_demo.dto.BookDTO;

import com.think.ms_demo.model.Book;
import com.think.ms_demo.response.BookResponseHandler;
import com.think.ms_demo.service.BookService;


@RestController
@RequestMapping("/books")
public class BookController {


    @Autowired
    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/welcome")
    public ResponseEntity<String> welcomeMessage()
    {
        return new ResponseEntity<>("Welcome to the Book Details", HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks()  
    {
        List<BookDTO> books = bookService.getAllBooks();
        if (books == null || books.isEmpty()) {
            return ResponseEntity.noContent().build();
        } return ResponseEntity.ok(books);
    }

    // Get a Particular Book from DB
    @GetMapping("{bookid}")
    public ResponseEntity<Object> getBookbyId(@PathVariable long bookid)
    {
        BookDTO book = bookService.getBook(bookid);
        if (book != null) {
            return BookResponseHandler.responseBuilder("Book Found", HttpStatus.OK, book);
        } else {
            return BookResponseHandler.responseBuilder("Book Not Found", HttpStatus.NOT_FOUND, null);
        }
    }
 
    @PostMapping("")
    public ResponseEntity<Object> addBooks(@RequestBody Book book) {
        bookService.addBooks(book);
        return BookResponseHandler.responseBuilder("Book Added Succesfully", HttpStatus.CREATED, book);
    }

    @PutMapping("/{bookid}")
    public ResponseEntity<Object> updateBooks(@PathVariable Long bookid,@RequestBody Book book)
    {
        bookService.updateBooks(bookid,book);
        return BookResponseHandler.responseBuilder("Book Details Updated Successfully", HttpStatus.OK, book);
    }

   // @PutMapping("/{bookid}/assignvendor")
   // public ResponseEntity<Object> assignVendorBook(
       // @PathVariable Long bookid,@RequestParam String vendorId) {

            //Book updatedBook = bookService.assignVendorBook(bookid, vendorId); // No longer returns Object
            //return BookResponseHandler.responseBuilder(
               // "Vendor assigned to book successfully",
               // HttpStatus.OK,
               // updatedBook // Return the updated book
       // );
  //  }

   // @ExceptionHandler(EntityNotFoundException.class) // Handle the exception
   // public ResponseEntity<Object> NotFoundBookException(EntityNotFoundException ex) {
      //  return BookResponseHandler.responseBuilder(
            //    ex.getMessage(), // Get the message from the exception
              //  HttpStatus.NOT_FOUND,
              //  null // No data needed in the body for this error
      //  );
   // }

    @DeleteMapping("/{bookid}")
    public ResponseEntity<String>  deleteBook(@PathVariable Long bookid)
    {
        boolean bookIsDeleted = bookService.deleteBook(bookid);
        if(bookIsDeleted)
        {
            return new ResponseEntity<>("Book Deleted Successfully",HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>("Book or Vendor Not Found",HttpStatus.NOT_FOUND);

        }
        
    }
}
