package com.ankitkumar.library.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ankitkumar.library.Repository.BookRepository;
import com.ankitkumar.library.model.Book;
import com.ankitkumar.library.model.Borrower;
import com.ankitkumar.library.service.BookService;

@ExtendWith(MockitoExtension.class)
public class ControllerTests {

	@Mock
	private BookService bookService;

	@InjectMocks
	private BorrowerController borrowerController;

	@InjectMocks
	private BookController bookController;

	@Mock
	private BookRepository bookRepository;

	@Test
	public void testRegisterBorrower_Success() {
		// Mock data
		Borrower borrower = new Borrower("John Doe", "john@example.com");
		when(bookService.registerBorrower(borrower)).thenReturn(borrower);

		// Perform the POST request to register a borrower
		ResponseEntity<Object> response = borrowerController.registerBorrower(borrower);

		// Verify the response
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(borrower, response.getBody());
	}

	@Test
	public void testRegisterBook_Success() {
		// Mock data
		Book book = new Book("1234567890", "Sample Book 1", "Author 1", true, null);
		when(bookService.registerBook(book)).thenReturn(book);

		// Perform the POST request to register a book
		ResponseEntity<Object> response = bookController.registerBook(book);

		// Verify the response
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(book, response.getBody());
	}

	@Test
	public void testGetAllBooks_Success() {
		// Mock data
		List<Book> allBooks = Arrays.asList(new Book("1234567890", "Sample Book 1", "Author 1", true, null),
				new Book("0987654321", "Sample Book 2", "Author 2", true, null));
		when(bookService.getAllAvailableBooks()).thenReturn(allBooks);

		// Perform the GET request to retrieve all books
		ResponseEntity<Object> response = bookController.getAllAvailableBooks();

		// Verify the response
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(allBooks, response.getBody());
	}

	@Test
	public void testBorrowBook_Success() {
		// Mock data
		long bookId = 1L;
		Borrower borrower = new Borrower("John Doe", "john@example.com");
		Book book = new Book("1234567890", "Sample Book", "Author", true, null);
		when(bookService.borrowBook(bookId, borrower.getId())).thenReturn(book);

		// Perform the POST request to borrow a book
		ResponseEntity<Object> response = borrowerController.borrowBook(bookId, borrower.getId());

		// Verify the response
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(book, response.getBody());
	}

	@Test
	public void testReturnBook_Success() {
		// Arrange
		long bookId = 1L;
		Book book = new Book("1234567890", "Sample Book", "Author", false, null);
		Borrower borrower = new Borrower("John Doe", "john@example.com");
		book.setId(bookId);
		book.setBorrower(borrower);

		TestBookService bookService = new TestBookService(bookRepository);

		// Act
		Book returnedBook = bookService.returnBook(bookId);

		// Assert
		assertEquals(null, returnedBook.getBorrower());
		assertEquals(true, returnedBook.isAvailable());

	}
}
