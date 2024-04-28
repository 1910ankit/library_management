package com.ankitkumar.library.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ankitkumar.library.Repository.BookRepository;
import com.ankitkumar.library.Repository.BorrowerRepository;
import com.ankitkumar.library.exception.BookNotFoundException;
import com.ankitkumar.library.exception.BookReturnException;
import com.ankitkumar.library.exception.BookUnavailableException;
import com.ankitkumar.library.exception.BorrowerNotFoundException;
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
        // Implement book registration logic here (e.g., validation, saving to database)
        return bookRepository.save(book);
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public List<Book> getAllAvailableBooks() {
        return bookRepository.findByAvailableTrue();
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
        } catch (BookUnavailableException e) {
            throw e;
        } catch (Exception e) {
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
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to return book", e);
        }
    }
}
