package com.think.ms_demo.exception.BookException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class BookExceptionController {

    @ExceptionHandler(value = {NotFoundBookException.class})
    public ResponseEntity<Object> handleNotFoundBookException(NotFoundBookException notFoundBookException)
    {
        BookException bookException = new BookException(
                 notFoundBookException.getMessage(),
                 notFoundBookException.getCause(),
                 HttpStatus.NOT_FOUND
         );
         return new ResponseEntity<>(bookException, HttpStatus.NOT_FOUND);
     }
    }
