package com.ankitkumar.library.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.autoconfigure.webservices.client.AutoConfigureMockWebServiceServer;

import com.ankitkumar.library.Repository.BookRepository;
import com.ankitkumar.library.model.Book;
import com.ankitkumar.library.model.Borrower;
import com.ankitkumar.library.service.BookService;

@AutoConfigureMockWebServiceServer
public class TestBookService implements BookService{
	
	private BookRepository bookRepository;
	
	// Constructor to inject the BookRepository
    public TestBookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
	@Override
	public Borrower registerBorrower(Borrower borrower) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Book registerBook(Book book) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Book> getAllAvailableBooks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Book borrowBook(Long bookId, Long borrowerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Book returnBook(Long bookId) {
		 // Arrange
	    Book book = new Book("1234567890", "Sample Book", "Author", false, null);
	    Borrower borrower = new Borrower("John Doe", "john@example.com");
	    book.setBorrower(borrower);
	    book.setId(bookId);
	    book.setBorrower(null);
	    book.setAvailable(true);
		return book;

	}

}
