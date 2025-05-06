package com.think.ms_demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.think.ms_demo.model.Book;

public interface BookRepository extends JpaRepository<Book,Long> {
    List<Book> findByBookid(Long bookid);

}
