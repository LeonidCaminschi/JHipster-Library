package com.esempla.library.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class BookAuthorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BookAuthor getBookAuthorSample1() {
        return new BookAuthor().id(1L);
    }

    public static BookAuthor getBookAuthorSample2() {
        return new BookAuthor().id(2L);
    }

    public static BookAuthor getBookAuthorRandomSampleGenerator() {
        return new BookAuthor().id(longCount.incrementAndGet());
    }
}
