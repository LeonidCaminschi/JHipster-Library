package com.esempla.library.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class BorrowedBookTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BorrowedBook getBorrowedBookSample1() {
        return new BorrowedBook().id(1L);
    }

    public static BorrowedBook getBorrowedBookSample2() {
        return new BorrowedBook().id(2L);
    }

    public static BorrowedBook getBorrowedBookRandomSampleGenerator() {
        return new BorrowedBook().id(longCount.incrementAndGet());
    }
}
