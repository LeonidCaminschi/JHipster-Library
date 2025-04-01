package com.esempla.library.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A BorrowedBook.
 */
@Entity
@Table(name = "borrowed_book")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BorrowedBook implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "borrow_date", nullable = false)
    private ZonedDateTime borrowDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "bookAuthors", "borrowedBooks", "publisher" }, allowSetters = true)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "borrowedBooks" }, allowSetters = true)
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BorrowedBook id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getBorrowDate() {
        return this.borrowDate;
    }

    public BorrowedBook borrowDate(ZonedDateTime borrowDate) {
        this.setBorrowDate(borrowDate);
        return this;
    }

    public void setBorrowDate(ZonedDateTime borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Book getBook() {
        return this.book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public BorrowedBook book(Book book) {
        this.setBook(book);
        return this;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public BorrowedBook client(Client client) {
        this.setClient(client);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BorrowedBook that = (BorrowedBook) o;
        return Objects.equals(id, that.id) && Objects.equals(borrowDate, that.borrowDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, borrowDate);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BorrowedBook{" +
            "id=" + getId() +
            ", borrowDate='" + getBorrowDate() + "'" +
            "}";
    }
}
