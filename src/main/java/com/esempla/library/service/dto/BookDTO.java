package com.esempla.library.service.dto;

public class BookDTO {

    private Long id;
    private String name;
    private String publishYear;
    private Long copies;
    private String picture;
    private Long publisherId;

    public BookDTO(Long id, String name, String publishYear, Long copies, String picture, Long publisherId) {
        this.id = id;
        this.name = name;
        this.publishYear = publishYear;
        this.copies = copies;
        this.picture = picture;
        this.publisherId = publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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
