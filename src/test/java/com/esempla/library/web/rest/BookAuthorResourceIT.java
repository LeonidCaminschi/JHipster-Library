package com.esempla.library.web.rest;

import static com.esempla.library.domain.BookAuthorAsserts.*;
import static com.esempla.library.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.esempla.library.IntegrationTest;
import com.esempla.library.domain.BookAuthor;
import com.esempla.library.repository.BookAuthorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BookAuthorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BookAuthorResourceIT {

    private static final String ENTITY_API_URL = "/api/book-authors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BookAuthorRepository bookAuthorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookAuthorMockMvc;

    private BookAuthor bookAuthor;

    private BookAuthor insertedBookAuthor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookAuthor createEntity() {
        return new BookAuthor();
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookAuthor createUpdatedEntity() {
        return new BookAuthor();
    }

    @BeforeEach
    public void initTest() {
        bookAuthor = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedBookAuthor != null) {
            bookAuthorRepository.delete(insertedBookAuthor);
            insertedBookAuthor = null;
        }
    }

    @Test
    @Transactional
    void createBookAuthor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BookAuthor
        var returnedBookAuthor = om.readValue(
            restBookAuthorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookAuthor)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BookAuthor.class
        );

        // Validate the BookAuthor in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertBookAuthorUpdatableFieldsEquals(returnedBookAuthor, getPersistedBookAuthor(returnedBookAuthor));

        insertedBookAuthor = returnedBookAuthor;
    }

    @Test
    @Transactional
    void createBookAuthorWithExistingId() throws Exception {
        // Create the BookAuthor with an existing ID
        bookAuthor.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookAuthorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookAuthor)))
            .andExpect(status().isBadRequest());

        // Validate the BookAuthor in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBookAuthors() throws Exception {
        // Initialize the database
        insertedBookAuthor = bookAuthorRepository.saveAndFlush(bookAuthor);

        // Get all the bookAuthorList
        restBookAuthorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookAuthor.getId().intValue())));
    }

    @Test
    @Transactional
    void getBookAuthor() throws Exception {
        // Initialize the database
        insertedBookAuthor = bookAuthorRepository.saveAndFlush(bookAuthor);

        // Get the bookAuthor
        restBookAuthorMockMvc
            .perform(get(ENTITY_API_URL_ID, bookAuthor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bookAuthor.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingBookAuthor() throws Exception {
        // Get the bookAuthor
        restBookAuthorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBookAuthor() throws Exception {
        // Initialize the database
        insertedBookAuthor = bookAuthorRepository.saveAndFlush(bookAuthor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookAuthor
        BookAuthor updatedBookAuthor = bookAuthorRepository.findById(bookAuthor.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBookAuthor are not directly saved in db
        em.detach(updatedBookAuthor);

        restBookAuthorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBookAuthor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedBookAuthor))
            )
            .andExpect(status().isOk());

        // Validate the BookAuthor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBookAuthorToMatchAllProperties(updatedBookAuthor);
    }

    @Test
    @Transactional
    void putNonExistingBookAuthor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookAuthor.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookAuthorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookAuthor.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookAuthor))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookAuthor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBookAuthor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookAuthor.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookAuthorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookAuthor))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookAuthor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBookAuthor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookAuthor.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookAuthorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookAuthor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookAuthor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookAuthorWithPatch() throws Exception {
        // Initialize the database
        insertedBookAuthor = bookAuthorRepository.saveAndFlush(bookAuthor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookAuthor using partial update
        BookAuthor partialUpdatedBookAuthor = new BookAuthor();
        partialUpdatedBookAuthor.setId(bookAuthor.getId());

        restBookAuthorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookAuthor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBookAuthor))
            )
            .andExpect(status().isOk());

        // Validate the BookAuthor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookAuthorUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBookAuthor, bookAuthor),
            getPersistedBookAuthor(bookAuthor)
        );
    }

    @Test
    @Transactional
    void fullUpdateBookAuthorWithPatch() throws Exception {
        // Initialize the database
        insertedBookAuthor = bookAuthorRepository.saveAndFlush(bookAuthor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookAuthor using partial update
        BookAuthor partialUpdatedBookAuthor = new BookAuthor();
        partialUpdatedBookAuthor.setId(bookAuthor.getId());

        restBookAuthorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookAuthor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBookAuthor))
            )
            .andExpect(status().isOk());

        // Validate the BookAuthor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookAuthorUpdatableFieldsEquals(partialUpdatedBookAuthor, getPersistedBookAuthor(partialUpdatedBookAuthor));
    }

    @Test
    @Transactional
    void patchNonExistingBookAuthor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookAuthor.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookAuthorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookAuthor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookAuthor))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookAuthor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBookAuthor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookAuthor.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookAuthorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookAuthor))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookAuthor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBookAuthor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookAuthor.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookAuthorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bookAuthor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookAuthor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBookAuthor() throws Exception {
        // Initialize the database
        insertedBookAuthor = bookAuthorRepository.saveAndFlush(bookAuthor);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bookAuthor
        restBookAuthorMockMvc
            .perform(delete(ENTITY_API_URL_ID, bookAuthor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bookAuthorRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected BookAuthor getPersistedBookAuthor(BookAuthor bookAuthor) {
        return bookAuthorRepository.findById(bookAuthor.getId()).orElseThrow();
    }

    protected void assertPersistedBookAuthorToMatchAllProperties(BookAuthor expectedBookAuthor) {
        assertBookAuthorAllPropertiesEquals(expectedBookAuthor, getPersistedBookAuthor(expectedBookAuthor));
    }

    protected void assertPersistedBookAuthorToMatchUpdatableProperties(BookAuthor expectedBookAuthor) {
        assertBookAuthorAllUpdatablePropertiesEquals(expectedBookAuthor, getPersistedBookAuthor(expectedBookAuthor));
    }
}
