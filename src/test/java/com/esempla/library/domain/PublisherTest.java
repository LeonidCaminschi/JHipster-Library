package com.esempla.library.domain;

import static com.esempla.library.domain.BookTestSamples.*;
import static com.esempla.library.domain.PublisherTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.esempla.library.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PublisherTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Publisher.class);
        Publisher publisher1 = getPublisherSample1();
        Publisher publisher2 = new Publisher();
        assertThat(publisher1).isNotEqualTo(publisher2);

        publisher2.setId(publisher1.getId());
        assertThat(publisher1).isEqualTo(publisher2);

        publisher2 = getPublisherSample2();
        assertThat(publisher1).isNotEqualTo(publisher2);
    }

    @Test
    void bookTest() {
        Publisher publisher = getPublisherRandomSampleGenerator();
        Book bookBack = getBookRandomSampleGenerator();

        publisher.addBook(bookBack);
        assertThat(publisher.getBooks()).containsOnly(bookBack);
        assertThat(bookBack.getPublisher()).isEqualTo(publisher);

        publisher.removeBook(bookBack);
        assertThat(publisher.getBooks()).doesNotContain(bookBack);
        assertThat(bookBack.getPublisher()).isNull();

        publisher.books(new HashSet<>(Set.of(bookBack)));
        assertThat(publisher.getBooks()).containsOnly(bookBack);
        assertThat(bookBack.getPublisher()).isEqualTo(publisher);

        publisher.setBooks(new HashSet<>());
        assertThat(publisher.getBooks()).doesNotContain(bookBack);
        assertThat(bookBack.getPublisher()).isNull();
    }
}
