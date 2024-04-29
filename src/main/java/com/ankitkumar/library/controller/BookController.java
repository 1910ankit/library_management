package com.ankitkumar.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.ankitkumar.library.Repository.BookRepository;
import com.ankitkumar.library.exception.BookRegistrationException;
import com.ankitkumar.library.exception.InconsistentBookException;
import com.ankitkumar.library.model.Book;
import com.ankitkumar.library.service.BookService;

@RestController
@RequestMapping("/api/books")
public class BookController {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private BookService bookService;

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("Internal server error occurred. Please try again later.");
	}

	@ExceptionHandler(value = { BookRegistrationException.class, InconsistentBookException.class,
			IllegalArgumentException.class })
	protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@PostMapping
	public ResponseEntity<Object> registerBook(@RequestBody Book book) {
		Book registeredBook;
		try {
			registeredBook = bookService.registerBook(book);
		} catch (RuntimeException e) {
			return handleConflict(e, null);
		}
		return ResponseEntity.ok(registeredBook);
	}

	@GetMapping("/available")
	public ResponseEntity<Object> getAllAvailableBooks() {
		try {
			List<Book> availableBooks = bookService.getAllAvailableBooks();
			return ResponseEntity.ok(availableBooks);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to retrieve available books. Please try again later.");
		}
	}

}
