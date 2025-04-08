package com.esempla.library.service.dto;

import com.esempla.library.domain.Book;
import com.opencsv.bean.CsvBindByName;

public class BookDTO {

    @CsvBindByName(column = "id")
    private Long id;

    @CsvBindByName(column = "book_name")
    private String name;

    @CsvBindByName(column = "publishYear")
    private String publishYear;

    @CsvBindByName(column = "copies")
    private Long copies;

    @CsvBindByName(column = "author_first_name")
    private String authorFirstName;

    @CsvBindByName(column = "author_last_name")
    private String authorLastName;

    @CsvBindByName(column = "publisher_name")
    private String publisherName;

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public BookDTO(
        Long id,
        String name,
        String publishYear,
        Long copies,
        String authorFirstName,
        String authorLastName,
        String publisherName
    ) {
        this.id = id;
        this.name = name;
        this.publishYear = publishYear;
        this.copies = copies;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
        this.publisherName = publisherName;
    }

    public Long getCopies() {
        return copies;
    }

    public void setCopies(Long copies) {
        this.copies = copies;
    }

    public String getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(String publishYear) {
        this.publishYear = publishYear;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
