package com.esempla.library.service.dto;

import com.esempla.library.export.CsvBindByNameOrder;
import com.opencsv.bean.CsvBindByName;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@CsvBindByNameOrder({ "Id", "Borrow date time", "Book name", "Client's first name", "Client's last name" })
public class BorrowedBookDTO {

    @CsvBindByName(column = "Id")
    private Long id;

    @CsvBindByName(column = "Borrow date time")
    private ZonedDateTime borrowDateTime;

    @CsvBindByName(column = "Book name")
    private String bookName;

    @CsvBindByName(column = "Client's first name")
    private String clientFirstName;

    @CsvBindByName(column = "Client's last name")
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
