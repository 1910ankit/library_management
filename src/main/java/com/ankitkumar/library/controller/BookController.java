package com.ankitkumar.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ankitkumar.library.Repository.BookRepository;
import com.ankitkumar.library.model.Book;
import com.ankitkumar.library.service.BookService;

@RestController
@RequestMapping("/api/books")
public class BookController {

	@Autowired
    private BookRepository bookRepository;
	
	@Autowired
    private BookService bookService;

    @PostMapping
    public ResponseEntity<Book> registerBook(@RequestBody Book book) {
        Book savedBook = bookRepository.save(book);
        return ResponseEntity.ok(savedBook);
    }
    
	/*
	 * @GetMapping("/{id}") public ResponseEntity<List<Book>> getAllBooks() {
	 * List<Book> books = bookRepository.findAll(); return ResponseEntity.ok(books);
	 * }
	 */
    
    @GetMapping("/available")
    public ResponseEntity<List<Book>> getAllAvailableBooks() {
        List<Book> availableBooks = bookService.getAllAvailableBooks();
        return ResponseEntity.ok(availableBooks);
    }
    
    
}
