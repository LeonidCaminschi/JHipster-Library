package com.esempla.library.service;

import static com.esempla.library.config.Constants.BOOKS_NOT_FOUND;
import static com.esempla.library.config.Constants.BOOK_NOT_BORROWED_BY_CLIENT;
import static com.esempla.library.config.Constants.CLIENT_NOT_FOUND;
import static com.esempla.library.config.Constants.NO_COPIES_AVAILABLE;

import com.esempla.library.domain.Book;
import com.esempla.library.domain.BorrowedBook;
import com.esempla.library.domain.Client;
import com.esempla.library.repository.BookRepository;
import com.esempla.library.repository.BorrowedBookRepository;
import com.esempla.library.repository.ClientRepository;
import com.esempla.library.service.dto.BorrowReturnRequest;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BorrowedBookService {

    private Logger logger = LoggerFactory.getLogger(BorrowedBookService.class);

    private final BorrowedBookRepository borrowedBookRepository;
    private final BookRepository bookRepository;
    private final ClientRepository clientRepository;

    public BorrowedBookService(
        BorrowedBookRepository borrowedBookRepository,
        BookRepository bookRepository,
        ClientRepository clientRepository
    ) {
        this.borrowedBookRepository = borrowedBookRepository;
        this.bookRepository = bookRepository;
        this.clientRepository = clientRepository;
    }

    public BorrowedBook save(BorrowedBook borrowedBook) {
        return borrowedBookRepository.save(borrowedBook);
    }

    public List<BorrowedBook> findAll() {
        return borrowedBookRepository.findAll();
    }

    public Optional<BorrowedBook> findById(Long id) {
        return borrowedBookRepository.findById(id);
    }

    public void deleteById(Long id) {
        borrowedBookRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return borrowedBookRepository.existsById(id);
    }

    public Optional<BorrowedBook> findByClientAndBook(Client client, Book book) {
        return borrowedBookRepository.findByClientAndBook(client, book);
    }

    public void deleteByClientAndBook(Client client, Book book) {
        borrowedBookRepository.deleteByClientAndBook(client, book);
    }

    public List<BorrowedBook> findAllByBookInAndClient(List<Book> books, Client client) {
        return borrowedBookRepository.findAllByBookInAndClient(books, client);
    }

    public int borrowBook(BorrowReturnRequest borrowReturnRequest) {
        var client = clientRepository
            .findByFirstNameAndLastName(borrowReturnRequest.clientFirstName(), borrowReturnRequest.clientLastName())
            .orElseThrow(() -> new IllegalArgumentException(CLIENT_NOT_FOUND));

        var books = bookRepository.findAllByNameIn(borrowReturnRequest.bookNames());
        if (books.isEmpty()) {
            throw new IllegalArgumentException(BOOKS_NOT_FOUND);
        }

        int availableBooks = 0;
        for (var book : books) {
            if (book.getCopies() <= 0) {
                logger.info(NO_COPIES_AVAILABLE + " {}", book.getName());
                continue;
            }

            var borrowedBook = new BorrowedBook();
            borrowedBook.setClient(client);
            borrowedBook.setBook(book);
            borrowedBook.setBorrowDate(ZonedDateTime.now());
            borrowedBookRepository.save(borrowedBook);

            book.setCopies(book.getCopies() - 1);
            availableBooks++;
        }

        return availableBooks;
    }

    public int returnBook(BorrowReturnRequest borrowReturnRequest) {
        var client = clientRepository
            .findByFirstNameAndLastName(borrowReturnRequest.clientFirstName(), borrowReturnRequest.clientLastName())
            .orElseThrow(() -> new IllegalArgumentException(CLIENT_NOT_FOUND));

        var books = bookRepository.findAllByNameIn(borrowReturnRequest.bookNames());
        if (books.isEmpty()) {
            throw new IllegalArgumentException(BOOKS_NOT_FOUND);
        }

        int returnedBooks = 0;
        for (var book : books) {
            var borrowedBook = borrowedBookRepository.findByClientAndBook(client, book);
            if (borrowedBook.isEmpty()) {
                logger.info(BOOK_NOT_BORROWED_BY_CLIENT + " {} {}", client.getFirstName(), client.getLastName());
                continue;
            }

            borrowedBookRepository.delete(borrowedBook.get());
            book.setCopies(book.getCopies() + 1);
            returnedBooks++;
        }

        return returnedBooks;
    }
}
