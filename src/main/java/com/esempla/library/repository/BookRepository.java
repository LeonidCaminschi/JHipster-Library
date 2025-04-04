package com.esempla.library.repository;

import com.esempla.library.domain.Book;
import com.esempla.library.service.dto.BookDTO;
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

    @Query(
        value = """
        Select b.id, b.name, b.publish_year, b.copies, a.first_name, a.last_name, pu.name from book as b
                INNER JOIN book_author as ba on b.id = ba.book_id
                INNER JOIN author as a on ba.author_id = a.id
                INNER JOIN publisher as pu on b.publisher_id = pu.id;
        """,
        nativeQuery = true
    )
    List<BookDTO> findAllAsDTO();
}
