package com.esempla.library.repository;

import com.esempla.library.domain.Book;
import com.esempla.library.domain.BorrowedBook;
import com.esempla.library.domain.Client;
import com.esempla.library.service.dto.BookDTO;
import com.esempla.library.service.dto.BorrowedBookDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BorrowedBook entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BorrowedBookRepository extends JpaRepository<BorrowedBook, Long> {
    Optional<BorrowedBook> findByClientAndBook(Client client, Book book);
    void deleteByClientAndBook(Client client, Book book);
    List<BorrowedBook> findAllByBookInAndClient(List<Book> book, Client client);

    @Query(
        value = """
        Select bb.id, bb.borrow_date, b.name, c.first_name, c.last_name from borrowed_book as bb
                INNER JOIN Book as b on bb.book_id = b.id
                INNER JOIN Client as c on bb.client_id = c.id;
        """,
        nativeQuery = true
    )
    List<BorrowedBookDTO> findAllAsDTO();
}
