package com.esempla.library.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Author.
 */
@Entity
@Table(name = "author")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
    @JsonIgnoreProperties(value = { "author", "book" }, allowSetters = true)
    private Set<BookAuthor> bookAuthors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Author id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Author firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Author lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<BookAuthor> getBookAuthors() {
        return this.bookAuthors;
    }

    public void setBookAuthors(Set<BookAuthor> bookAuthors) {
        if (this.bookAuthors != null) {
            this.bookAuthors.forEach(i -> i.setAuthor(null));
        }
        if (bookAuthors != null) {
            bookAuthors.forEach(i -> i.setAuthor(this));
        }
        this.bookAuthors = bookAuthors;
    }

    public Author bookAuthors(Set<BookAuthor> bookAuthors) {
        this.setBookAuthors(bookAuthors);
        return this;
    }

    public Author addBookAuthor(BookAuthor bookAuthor) {
        this.bookAuthors.add(bookAuthor);
        bookAuthor.setAuthor(this);
        return this;
    }

    public Author removeBookAuthor(BookAuthor bookAuthor) {
        this.bookAuthors.remove(bookAuthor);
        bookAuthor.setAuthor(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(id, author.id) && Objects.equals(firstName, author.firstName) && Objects.equals(lastName, author.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Author{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            "}";
    }
}
