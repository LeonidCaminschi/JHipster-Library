package com.esempla.library.repository;

import com.esempla.library.domain.BookAuthor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BookAuthor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookAuthorRepository extends JpaRepository<BookAuthor, Long> {}
