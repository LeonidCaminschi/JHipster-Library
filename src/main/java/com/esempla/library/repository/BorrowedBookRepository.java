package com.esempla.library.repository;

import com.esempla.library.domain.Book;
import com.esempla.library.domain.BorrowedBook;
import com.esempla.library.domain.Client;
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
}
