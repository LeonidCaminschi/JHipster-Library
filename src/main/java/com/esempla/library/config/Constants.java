package com.esempla.library.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

    public static final String SYSTEM = "system";
    public static final String DEFAULT_LANGUAGE = "ro";

    public static final String CLIENT_NOT_FOUND = "Client not found";
    public static final String BOOKS_NOT_FOUND = "Books not found";
    public static final String NO_COPIES_AVAILABLE = "No copies available";
    public static final String BOOK_NOT_BORROWED_BY_CLIENT = "Book not borrowed by client";

    public static final String ENTITY_NAME = "borrowedBook";
    public static final String IDEXISTS = "idexists";
    public static final String A_NEW_BORROWED_BOOK_CANNOT_ALREADY_HAVE_AN_ID = "A new borrowedBook cannot already have an ID";
    public static final String INVALID_ID = "Invalid id";
    public static final String IDNULL = "idnull";
    public static final String IDINVALID = "idinvalid";
    public static final String ENTITY_NOT_FOUND = "Entity not found";
    public static final String IDNOTFOUND = "idnotfound";

    private Constants() {}
}
