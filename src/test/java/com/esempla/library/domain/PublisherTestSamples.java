package com.esempla.library.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PublisherTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Publisher getPublisherSample1() {
        return new Publisher().id(1L).name("name1");
    }

    public static Publisher getPublisherSample2() {
        return new Publisher().id(2L).name("name2");
    }

    public static Publisher getPublisherRandomSampleGenerator() {
        return new Publisher().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
