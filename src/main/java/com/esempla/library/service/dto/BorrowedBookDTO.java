package com.esempla.library.service.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class BorrowedBookDTO {

    private Long id;
    private ZonedDateTime borrowDateTime;
    private String bookName;
    private String clientFirstName;
    private String clientLastName;

    public BorrowedBookDTO(Long id, Timestamp borrowDateTime, String bookName, String clientFirstName, String clientLastName) {
        this.id = id;
        this.borrowDateTime = borrowDateTime.toInstant().atZone(ZonedDateTime.now().getZone());
        this.bookName = bookName;
        this.clientFirstName = clientFirstName;
        this.clientLastName = clientLastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getBorrowDateTime() {
        return borrowDateTime.toLocalDateTime();
    }

    public void setBorrowDateTime(ZonedDateTime borrowDateTime) {
        this.borrowDateTime = borrowDateTime;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getClientFirstName() {
        return clientFirstName;
    }

    public void setClientFirstName(String clientFirstName) {
        this.clientFirstName = clientFirstName;
    }

    public String getClientLastName() {
        return clientLastName;
    }

    public void setClientLastName(String clientLastName) {
        this.clientLastName = clientLastName;
    }
}
