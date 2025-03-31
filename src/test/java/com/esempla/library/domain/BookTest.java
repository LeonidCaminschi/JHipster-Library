package com.esempla.library.domain;

import static com.esempla.library.domain.BookAuthorTestSamples.*;
import static com.esempla.library.domain.BookTestSamples.*;
import static com.esempla.library.domain.BorrowedBookTestSamples.*;
import static com.esempla.library.domain.PublisherTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.esempla.library.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BookTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Book.class);
        Book book1 = getBookSample1();
        Book book2 = new Book();
        assertThat(book1).isNotEqualTo(book2);

        book2.setId(book1.getId());
        assertThat(book1).isEqualTo(book2);

        book2 = getBookSample2();
        assertThat(book1).isNotEqualTo(book2);
    }

    @Test
    void bookAuthorTest() {
        Book book = getBookRandomSampleGenerator();
        BookAuthor bookAuthorBack = getBookAuthorRandomSampleGenerator();

        book.addBookAuthor(bookAuthorBack);
        assertThat(book.getBookAuthors()).containsOnly(bookAuthorBack);
        assertThat(bookAuthorBack.getBook()).isEqualTo(book);

        book.removeBookAuthor(bookAuthorBack);
        assertThat(book.getBookAuthors()).doesNotContain(bookAuthorBack);
        assertThat(bookAuthorBack.getBook()).isNull();

        book.bookAuthors(new HashSet<>(Set.of(bookAuthorBack)));
        assertThat(book.getBookAuthors()).containsOnly(bookAuthorBack);
        assertThat(bookAuthorBack.getBook()).isEqualTo(book);

        book.setBookAuthors(new HashSet<>());
        assertThat(book.getBookAuthors()).doesNotContain(bookAuthorBack);
        assertThat(bookAuthorBack.getBook()).isNull();
    }

    @Test
    void borrowedBookTest() {
        Book book = getBookRandomSampleGenerator();
        BorrowedBook borrowedBookBack = getBorrowedBookRandomSampleGenerator();

        book.addBorrowedBook(borrowedBookBack);
        assertThat(book.getBorrowedBooks()).containsOnly(borrowedBookBack);
        assertThat(borrowedBookBack.getBook()).isEqualTo(book);

        book.removeBorrowedBook(borrowedBookBack);
        assertThat(book.getBorrowedBooks()).doesNotContain(borrowedBookBack);
        assertThat(borrowedBookBack.getBook()).isNull();

        book.borrowedBooks(new HashSet<>(Set.of(borrowedBookBack)));
        assertThat(book.getBorrowedBooks()).containsOnly(borrowedBookBack);
        assertThat(borrowedBookBack.getBook()).isEqualTo(book);

        book.setBorrowedBooks(new HashSet<>());
        assertThat(book.getBorrowedBooks()).doesNotContain(borrowedBookBack);
        assertThat(borrowedBookBack.getBook()).isNull();
    }

    @Test
    void publisherTest() {
        Book book = getBookRandomSampleGenerator();
        Publisher publisherBack = getPublisherRandomSampleGenerator();

        book.setPublisher(publisherBack);
        assertThat(book.getPublisher()).isEqualTo(publisherBack);

        book.publisher(null);
        assertThat(book.getPublisher()).isNull();
    }
}
