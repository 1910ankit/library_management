package com.ankitkumar.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ankitkumar.library.Repository.BorrowerRepository;
import com.ankitkumar.library.model.Book;
import com.ankitkumar.library.model.Borrower;
import com.ankitkumar.library.service.BookService;

@RestController
@RequestMapping("/api/borrowers")
public class BorrowerController {
	
	@Autowired
    private BorrowerRepository borrowerRepository;
	
	@Autowired
    private BookService bookService;
	
	@PostMapping
	public ResponseEntity<Borrower> registerBorrower(@RequestBody Borrower borrower){
		
		Borrower savedBorrower = borrowerRepository.save(borrower);
        return ResponseEntity.ok(savedBorrower);
	}

	@PostMapping("/{bookId}/borrow/{borrowerId}")
    public ResponseEntity<Book> borrowBook(@PathVariable Long bookId, @PathVariable Long borrowerId) {
        Book borrowedBook = bookService.borrowBook(bookId, borrowerId);
        if (borrowedBook != null) {
            return ResponseEntity.ok(borrowedBook);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{bookId}/return")
    public ResponseEntity<Book> returnBook(@PathVariable Long bookId) {
        Book returnedBook = bookService.returnBook(bookId);
        if (returnedBook != null) {
            return ResponseEntity.ok(returnedBook);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
