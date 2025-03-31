package com.esempla.library.domain;

import static com.esempla.library.domain.BorrowedBookTestSamples.*;
import static com.esempla.library.domain.ClientTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.esempla.library.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ClientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Client.class);
        Client client1 = getClientSample1();
        Client client2 = new Client();
        assertThat(client1).isNotEqualTo(client2);

        client2.setId(client1.getId());
        assertThat(client1).isEqualTo(client2);

        client2 = getClientSample2();
        assertThat(client1).isNotEqualTo(client2);
    }

    @Test
    void borrowedBookTest() {
        Client client = getClientRandomSampleGenerator();
        BorrowedBook borrowedBookBack = getBorrowedBookRandomSampleGenerator();

        client.addBorrowedBook(borrowedBookBack);
        assertThat(client.getBorrowedBooks()).containsOnly(borrowedBookBack);
        assertThat(borrowedBookBack.getClient()).isEqualTo(client);

        client.removeBorrowedBook(borrowedBookBack);
        assertThat(client.getBorrowedBooks()).doesNotContain(borrowedBookBack);
        assertThat(borrowedBookBack.getClient()).isNull();

        client.borrowedBooks(new HashSet<>(Set.of(borrowedBookBack)));
        assertThat(client.getBorrowedBooks()).containsOnly(borrowedBookBack);
        assertThat(borrowedBookBack.getClient()).isEqualTo(client);

        client.setBorrowedBooks(new HashSet<>());
        assertThat(client.getBorrowedBooks()).doesNotContain(borrowedBookBack);
        assertThat(borrowedBookBack.getClient()).isNull();
    }
}
