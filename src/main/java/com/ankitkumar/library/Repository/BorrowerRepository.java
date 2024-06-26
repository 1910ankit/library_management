package com.ankitkumar.library.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ankitkumar.library.model.Borrower;

@Repository
public interface BorrowerRepository extends JpaRepository<Borrower, Long> {

	Optional<Borrower> findByEmail(String email);
}
