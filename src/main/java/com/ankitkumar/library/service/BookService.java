package com.ankitkumar.library.service;

import java.util.List;

import com.ankitkumar.library.model.Book;
import com.ankitkumar.library.model.Borrower;

public interface BookService {
	
	Borrower registerBorrower(Borrower borrower);

	Book registerBook(Book book);
    
    List<Book> getAllAvailableBooks();
    
    Book borrowBook(Long bookId, Long borrowerId);

    Book returnBook(Long bookId);
}
