package com.ankitkumar.library.service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.ankitkumar.library.Repository.BookRepository;
import com.ankitkumar.library.Repository.BorrowerRepository;
import com.ankitkumar.library.exception.BookNotFoundException;
import com.ankitkumar.library.exception.BookRegistrationException;
import com.ankitkumar.library.exception.BookReturnException;
import com.ankitkumar.library.exception.BookUnavailableException;
import com.ankitkumar.library.exception.BorrowerNotFoundException;
import com.ankitkumar.library.exception.BorrowerRegistrationException;
import com.ankitkumar.library.exception.DuplicateEmailException;
import com.ankitkumar.library.exception.InconsistentBookException;
import com.ankitkumar.library.model.Book;
import com.ankitkumar.library.model.Borrower;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private BorrowerRepository borrowerRepository;

	@Override
	public Book registerBook(Book book) {
		// Implement book registration logic here
		try {
			validateBookData(book);
			List<Book> existingBooks = bookRepository.findByIsbn(book.getIsbn());
			{
				// Ensure consistency for books with the same ISBN
				boolean isConsistent = existingBooks.stream()
						.allMatch(existingBook -> existingBook.getTitle().equals(book.getTitle())
								&& existingBook.getAuthor().equals(book.getAuthor()));
				if (!isConsistent) {
					throw new InconsistentBookException("Books with the same ISBN must have the same title and author");
				}
			}
			return bookRepository.save(book);
		} 
		catch(IllegalArgumentException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		catch(InconsistentBookException e) {
			throw new InconsistentBookException("Books with the same ISBN must have the same title and author");
		}
		catch (Exception e) {
			throw new BookRegistrationException("Failed to register book", e);
		}
	}

	@Override
	public Borrower registerBorrower(Borrower borrower) {
		try {
			validateBorrowerData(borrower);
			Optional<Borrower> existingBorrower = borrowerRepository.findByEmail(borrower.getEmail());
			if (existingBorrower.isPresent()) {
				throw new DuplicateEmailException("Email already exists");
			}
		} 
		catch(DuplicateEmailException e) {
			throw new DuplicateEmailException("Email already exists");
		}
		catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e.getMessage());
		} 
		catch (Exception e) {
			throw new BorrowerRegistrationException("Failed to register borrower", e);
		}
		return borrowerRepository.save(borrower);
	}

	@Override
	public List<Book> getAllAvailableBooks() {
		try {
			return bookRepository.findByAvailableTrue();
		} catch (Exception e) {
			throw new RuntimeException(e.getCause());
		}
		
	}

	@Override
	public Book borrowBook(Long bookId, Long borrowerId) {
		// Validate borrower data
		Borrower borrower = borrowerRepository.findById(borrowerId)
				.orElseThrow(() -> new BorrowerNotFoundException("Borrower not found with id: " + borrowerId));

		// Try to borrow the book
		try {
			return bookRepository.findById(bookId).map(book -> {
				if (book.isAvailable()) {
					book.setBorrower(borrower);
					book.setAvailable(false);
					return bookRepository.save(book);
				} else {
					throw new BookUnavailableException("Book is not available for borrowing");
				}
			}).orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));
		} 
		catch(BorrowerNotFoundException e) {
			throw new BorrowerNotFoundException(e.getMessage());
		}
		catch (BookUnavailableException e) {
			throw new BookUnavailableException(e.getMessage());
		}
		catch (BookNotFoundException e) {
			throw new BookNotFoundException(e.getMessage());
		} 
		catch (Exception e) {
			throw new RuntimeException("Failed to borrow book", e);
		}
	}

	@Override
	public Book returnBook(Long bookId) {
		// Try to return the book
		try {
			return bookRepository.findById(bookId).map(book -> {
				if (!book.isAvailable()) {
					book.setBorrower(null);
					book.setAvailable(true);
					return bookRepository.save(book);
				} else {
					throw new BookReturnException("Book is already available");
				}
			}).orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));
		} catch (BookReturnException e) {
			throw new BookReturnException(e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException("Failed to return book", e);
		}
	}

	// Validate book data
	private void validateBookData(Book book) {
		if (book.getIsbn() == null || book.getIsbn().isEmpty()) {
			throw new IllegalArgumentException("ISBN is required");
		}
		if (book.getTitle() == null || book.getTitle().isEmpty()) {
			throw new IllegalArgumentException("Title is required");
		}
		if (book.getAuthor() == null || book.getAuthor().isEmpty()) {
			throw new IllegalArgumentException("Author is required");
		}
	}

	// Validate book data
	private void validateBorrowerData(Borrower borrower) {
		if (borrower.getEmail() == null || borrower.getEmail().isEmpty()) {
			throw new IllegalArgumentException("Email is required");
		} else if (!isValidEmail(borrower.getEmail())) {
			throw new IllegalArgumentException("Invalid email format");
		}
		if (borrower.getName() == null || borrower.getName().isEmpty()) {
			throw new IllegalArgumentException("Name is required");
		}
	}

	// Check if email is valid using regex
	private boolean isValidEmail(String email) {
		// Regular expression for email validation
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		// Compile the regex pattern
		Pattern pattern = Pattern.compile(emailRegex);
		// Validate email against the regex pattern
		return pattern.matcher(email).matches();
	}
}
