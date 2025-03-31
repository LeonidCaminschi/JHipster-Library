package com.esempla.library.domain;

import static com.esempla.library.domain.AuthorTestSamples.*;
import static com.esempla.library.domain.BookAuthorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.esempla.library.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AuthorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Author.class);
        Author author1 = getAuthorSample1();
        Author author2 = new Author();
        assertThat(author1).isNotEqualTo(author2);

        author2.setId(author1.getId());
        assertThat(author1).isEqualTo(author2);

        author2 = getAuthorSample2();
        assertThat(author1).isNotEqualTo(author2);
    }

    @Test
    void bookAuthorTest() {
        Author author = getAuthorRandomSampleGenerator();
        BookAuthor bookAuthorBack = getBookAuthorRandomSampleGenerator();

        author.addBookAuthor(bookAuthorBack);
        assertThat(author.getBookAuthors()).containsOnly(bookAuthorBack);
        assertThat(bookAuthorBack.getAuthor()).isEqualTo(author);

        author.removeBookAuthor(bookAuthorBack);
        assertThat(author.getBookAuthors()).doesNotContain(bookAuthorBack);
        assertThat(bookAuthorBack.getAuthor()).isNull();

        author.bookAuthors(new HashSet<>(Set.of(bookAuthorBack)));
        assertThat(author.getBookAuthors()).containsOnly(bookAuthorBack);
        assertThat(bookAuthorBack.getAuthor()).isEqualTo(author);

        author.setBookAuthors(new HashSet<>());
        assertThat(author.getBookAuthors()).doesNotContain(bookAuthorBack);
        assertThat(bookAuthorBack.getAuthor()).isNull();
    }
}
