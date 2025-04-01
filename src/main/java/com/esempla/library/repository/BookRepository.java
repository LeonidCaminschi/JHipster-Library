package com.esempla.library.repository;

import com.esempla.library.domain.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Data JPA repository for the Book entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Transactional
    @Modifying
    @Query("update Book b set b.copies = b.copies - 1 where b.id = ?1")
    void updateCopiesById(Long id);

    List<Book> findAllByNameIn(List<String> name);
}
