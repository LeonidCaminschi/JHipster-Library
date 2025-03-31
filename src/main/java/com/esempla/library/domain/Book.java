package com.esempla.library.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Book.
 */
@Entity
@Table(name = "book")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(name = "publish_year", nullable = false)
    private String publishYear;

    @Min(value = 0L)
    @Column(name = "copies")
    private Long copies;

    @NotNull
    @Column(name = "picture", nullable = false)
    private String picture;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
    @JsonIgnoreProperties(value = { "author", "book" }, allowSetters = true)
    private Set<BookAuthor> bookAuthors = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
    @JsonIgnoreProperties(value = { "book", "client" }, allowSetters = true)
    private Set<BorrowedBook> borrowedBooks = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "books" }, allowSetters = true)
    private Publisher publisher;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Book id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Book name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublishYear() {
        return this.publishYear;
    }

    public Book publishYear(String publishYear) {
        this.setPublishYear(publishYear);
        return this;
    }

    public void setPublishYear(String publishYear) {
        this.publishYear = publishYear;
    }

    public Long getCopies() {
        return this.copies;
    }

    public Book copies(Long copies) {
        this.setCopies(copies);
        return this;
    }

    public void setCopies(Long copies) {
        this.copies = copies;
    }

    public String getPicture() {
        return this.picture;
    }

    public Book picture(String picture) {
        this.setPicture(picture);
        return this;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Set<BookAuthor> getBookAuthors() {
        return this.bookAuthors;
    }

    public void setBookAuthors(Set<BookAuthor> bookAuthors) {
        if (this.bookAuthors != null) {
            this.bookAuthors.forEach(i -> i.setBook(null));
        }
        if (bookAuthors != null) {
            bookAuthors.forEach(i -> i.setBook(this));
        }
        this.bookAuthors = bookAuthors;
    }

    public Book bookAuthors(Set<BookAuthor> bookAuthors) {
        this.setBookAuthors(bookAuthors);
        return this;
    }

    public Book addBookAuthor(BookAuthor bookAuthor) {
        this.bookAuthors.add(bookAuthor);
        bookAuthor.setBook(this);
        return this;
    }

    public Book removeBookAuthor(BookAuthor bookAuthor) {
        this.bookAuthors.remove(bookAuthor);
        bookAuthor.setBook(null);
        return this;
    }

    public Set<BorrowedBook> getBorrowedBooks() {
        return this.borrowedBooks;
    }

    public void setBorrowedBooks(Set<BorrowedBook> borrowedBooks) {
        if (this.borrowedBooks != null) {
            this.borrowedBooks.forEach(i -> i.setBook(null));
        }
        if (borrowedBooks != null) {
            borrowedBooks.forEach(i -> i.setBook(this));
        }
        this.borrowedBooks = borrowedBooks;
    }

    public Book borrowedBooks(Set<BorrowedBook> borrowedBooks) {
        this.setBorrowedBooks(borrowedBooks);
        return this;
    }

    public Book addBorrowedBook(BorrowedBook borrowedBook) {
        this.borrowedBooks.add(borrowedBook);
        borrowedBook.setBook(this);
        return this;
    }

    public Book removeBorrowedBook(BorrowedBook borrowedBook) {
        this.borrowedBooks.remove(borrowedBook);
        borrowedBook.setBook(null);
        return this;
    }

    public Publisher getPublisher() {
        return this.publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Book publisher(Publisher publisher) {
        this.setPublisher(publisher);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Book)) {
            return false;
        }
        return getId() != null && getId().equals(((Book) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Book{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", publishYear='" + getPublishYear() + "'" +
            ", copies=" + getCopies() +
            ", picture='" + getPicture() + "'" +
            "}";
    }
}
