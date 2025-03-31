package com.esempla.library.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AuthorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Author getAuthorSample1() {
        return new Author().id(1L).firstName("firstName1").lastName("lastName1");
    }

    public static Author getAuthorSample2() {
        return new Author().id(2L).firstName("firstName2").lastName("lastName2");
    }

    public static Author getAuthorRandomSampleGenerator() {
        return new Author().id(longCount.incrementAndGet()).firstName(UUID.randomUUID().toString()).lastName(UUID.randomUUID().toString());
    }
}
