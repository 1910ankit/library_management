package com.ankitkumar.library.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ankitkumar.library.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	
	List<Book> findByAvailableTrue();
	
	List<Book> findByIsbn(String isbn);

}
