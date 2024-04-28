package com.ankitkumar.library.service;

import java.util.List;

import com.ankitkumar.library.model.Book;

public interface BookService {

	Book registerBook(Book book);
    
    Book getBookById(Long id);
    
    List<Book> getAllAvailableBooks();
    
    Book borrowBook(Long bookId, Long borrowerId);

    Book returnBook(Long bookId);
}
