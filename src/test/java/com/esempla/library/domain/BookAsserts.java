package com.esempla.library.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class BookAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBookAllPropertiesEquals(Book expected, Book actual) {
        assertBookAutoGeneratedPropertiesEquals(expected, actual);
        assertBookAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBookAllUpdatablePropertiesEquals(Book expected, Book actual) {
        assertBookUpdatableFieldsEquals(expected, actual);
        assertBookUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBookAutoGeneratedPropertiesEquals(Book expected, Book actual) {
        assertThat(actual)
            .as("Verify Book auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBookUpdatableFieldsEquals(Book expected, Book actual) {
        assertThat(actual)
            .as("Verify Book relevant properties")
            .satisfies(a -> assertThat(a.getName()).as("check name").isEqualTo(expected.getName()))
            .satisfies(a -> assertThat(a.getPublishYear()).as("check publishYear").isEqualTo(expected.getPublishYear()))
            .satisfies(a -> assertThat(a.getCopies()).as("check copies").isEqualTo(expected.getCopies()))
            .satisfies(a -> assertThat(a.getPicture()).as("check picture").isEqualTo(expected.getPicture()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBookUpdatableRelationshipsEquals(Book expected, Book actual) {
        assertThat(actual)
            .as("Verify Book relationships")
            .satisfies(a -> assertThat(a.getPublisher()).as("check publisher").isEqualTo(expected.getPublisher()));
    }
}
