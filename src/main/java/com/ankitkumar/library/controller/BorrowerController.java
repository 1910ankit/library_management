package com.ankitkumar.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.ankitkumar.library.exception.BookUnavailableException;
import com.ankitkumar.library.exception.BorrowerNotFoundException;
import com.ankitkumar.library.exception.DuplicateEmailException;
import com.ankitkumar.library.model.Book;
import com.ankitkumar.library.model.Borrower;
import com.ankitkumar.library.service.BookService;

@RestController
@RequestMapping("/api/borrowers")
public class BorrowerController {

	@Autowired
	private BookService bookService;

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("Internal server error occurred. Please try again later.");
	}

	@ExceptionHandler(value = { DuplicateEmailException.class, IllegalArgumentException.class,
			BookUnavailableException.class, BorrowerNotFoundException.class })
	protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
		//return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}

	@PostMapping
	public ResponseEntity<Object> registerBorrower(@RequestBody Borrower borrower) {
		Borrower savedBorrower;
		try {
			savedBorrower = bookService.registerBorrower(borrower);

		} catch (RuntimeException e) {
			return handleConflict(e, null);
		}
		return ResponseEntity.ok(savedBorrower);
	}

	@PostMapping("/{bookId}/borrow/{borrowerId}")
	public ResponseEntity<Object> borrowBook(@PathVariable Long bookId, @PathVariable Long borrowerId) {
		try {

			Book borrowedBook = bookService.borrowBook(bookId, borrowerId);
			if (borrowedBook != null) {
				return ResponseEntity.ok(borrowedBook);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (RuntimeException e) {
			return handleConflict(e, null);
		}
	}

	@PostMapping("/{bookId}/return")
	public ResponseEntity<Object> returnBook(@PathVariable Long bookId) {
		try {
			Book returnedBook = bookService.returnBook(bookId);
			if (returnedBook != null) {
				return ResponseEntity.ok(returnedBook);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (RuntimeException e) {
			return handleConflict(e, null);
		}
	}
}
