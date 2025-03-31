package com.esempla.library.web.rest;

import static com.esempla.library.domain.BorrowedBookAsserts.*;
import static com.esempla.library.web.rest.TestUtil.createUpdateProxyForBean;
import static com.esempla.library.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.esempla.library.IntegrationTest;
import com.esempla.library.domain.BorrowedBook;
import com.esempla.library.repository.BorrowedBookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link BorrowedBookResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BorrowedBookResourceIT {

    private static final ZonedDateTime DEFAULT_BORROW_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_BORROW_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/borrowed-books";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BorrowedBookRepository borrowedBookRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBorrowedBookMockMvc;

    private BorrowedBook borrowedBook;

    private BorrowedBook insertedBorrowedBook;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BorrowedBook createEntity() {
        return new BorrowedBook().borrowDate(DEFAULT_BORROW_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BorrowedBook createUpdatedEntity() {
        return new BorrowedBook().borrowDate(UPDATED_BORROW_DATE);
    }

    @BeforeEach
    public void initTest() {
        borrowedBook = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedBorrowedBook != null) {
            borrowedBookRepository.delete(insertedBorrowedBook);
            insertedBorrowedBook = null;
        }
    }

    @Test
    @Transactional
    void createBorrowedBook() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BorrowedBook
        var returnedBorrowedBook = om.readValue(
            restBorrowedBookMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(borrowedBook)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BorrowedBook.class
        );

        // Validate the BorrowedBook in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertBorrowedBookUpdatableFieldsEquals(returnedBorrowedBook, getPersistedBorrowedBook(returnedBorrowedBook));

        insertedBorrowedBook = returnedBorrowedBook;
    }

    @Test
    @Transactional
    void createBorrowedBookWithExistingId() throws Exception {
        // Create the BorrowedBook with an existing ID
        borrowedBook.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBorrowedBookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(borrowedBook)))
            .andExpect(status().isBadRequest());

        // Validate the BorrowedBook in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBorrowDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        borrowedBook.setBorrowDate(null);

        // Create the BorrowedBook, which fails.

        restBorrowedBookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(borrowedBook)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBorrowedBooks() throws Exception {
        // Initialize the database
        insertedBorrowedBook = borrowedBookRepository.saveAndFlush(borrowedBook);

        // Get all the borrowedBookList
        restBorrowedBookMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(borrowedBook.getId().intValue())))
            .andExpect(jsonPath("$.[*].borrowDate").value(hasItem(sameInstant(DEFAULT_BORROW_DATE))));
    }

    @Test
    @Transactional
    void getBorrowedBook() throws Exception {
        // Initialize the database
        insertedBorrowedBook = borrowedBookRepository.saveAndFlush(borrowedBook);

        // Get the borrowedBook
        restBorrowedBookMockMvc
            .perform(get(ENTITY_API_URL_ID, borrowedBook.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(borrowedBook.getId().intValue()))
            .andExpect(jsonPath("$.borrowDate").value(sameInstant(DEFAULT_BORROW_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingBorrowedBook() throws Exception {
        // Get the borrowedBook
        restBorrowedBookMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBorrowedBook() throws Exception {
        // Initialize the database
        insertedBorrowedBook = borrowedBookRepository.saveAndFlush(borrowedBook);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the borrowedBook
        BorrowedBook updatedBorrowedBook = borrowedBookRepository.findById(borrowedBook.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBorrowedBook are not directly saved in db
        em.detach(updatedBorrowedBook);
        updatedBorrowedBook.borrowDate(UPDATED_BORROW_DATE);

        restBorrowedBookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBorrowedBook.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedBorrowedBook))
            )
            .andExpect(status().isOk());

        // Validate the BorrowedBook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBorrowedBookToMatchAllProperties(updatedBorrowedBook);
    }

    @Test
    @Transactional
    void putNonExistingBorrowedBook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        borrowedBook.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBorrowedBookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, borrowedBook.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(borrowedBook))
            )
            .andExpect(status().isBadRequest());

        // Validate the BorrowedBook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBorrowedBook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        borrowedBook.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBorrowedBookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(borrowedBook))
            )
            .andExpect(status().isBadRequest());

        // Validate the BorrowedBook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBorrowedBook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        borrowedBook.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBorrowedBookMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(borrowedBook)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BorrowedBook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBorrowedBookWithPatch() throws Exception {
        // Initialize the database
        insertedBorrowedBook = borrowedBookRepository.saveAndFlush(borrowedBook);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the borrowedBook using partial update
        BorrowedBook partialUpdatedBorrowedBook = new BorrowedBook();
        partialUpdatedBorrowedBook.setId(borrowedBook.getId());

        partialUpdatedBorrowedBook.borrowDate(UPDATED_BORROW_DATE);

        restBorrowedBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBorrowedBook.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBorrowedBook))
            )
            .andExpect(status().isOk());

        // Validate the BorrowedBook in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBorrowedBookUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBorrowedBook, borrowedBook),
            getPersistedBorrowedBook(borrowedBook)
        );
    }

    @Test
    @Transactional
    void fullUpdateBorrowedBookWithPatch() throws Exception {
        // Initialize the database
        insertedBorrowedBook = borrowedBookRepository.saveAndFlush(borrowedBook);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the borrowedBook using partial update
        BorrowedBook partialUpdatedBorrowedBook = new BorrowedBook();
        partialUpdatedBorrowedBook.setId(borrowedBook.getId());

        partialUpdatedBorrowedBook.borrowDate(UPDATED_BORROW_DATE);

        restBorrowedBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBorrowedBook.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBorrowedBook))
            )
            .andExpect(status().isOk());

        // Validate the BorrowedBook in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBorrowedBookUpdatableFieldsEquals(partialUpdatedBorrowedBook, getPersistedBorrowedBook(partialUpdatedBorrowedBook));
    }

    @Test
    @Transactional
    void patchNonExistingBorrowedBook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        borrowedBook.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBorrowedBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, borrowedBook.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(borrowedBook))
            )
            .andExpect(status().isBadRequest());

        // Validate the BorrowedBook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBorrowedBook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        borrowedBook.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBorrowedBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(borrowedBook))
            )
            .andExpect(status().isBadRequest());

        // Validate the BorrowedBook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBorrowedBook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        borrowedBook.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBorrowedBookMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(borrowedBook)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BorrowedBook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBorrowedBook() throws Exception {
        // Initialize the database
        insertedBorrowedBook = borrowedBookRepository.saveAndFlush(borrowedBook);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the borrowedBook
        restBorrowedBookMockMvc
            .perform(delete(ENTITY_API_URL_ID, borrowedBook.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return borrowedBookRepository.count();
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

    protected BorrowedBook getPersistedBorrowedBook(BorrowedBook borrowedBook) {
        return borrowedBookRepository.findById(borrowedBook.getId()).orElseThrow();
    }

    protected void assertPersistedBorrowedBookToMatchAllProperties(BorrowedBook expectedBorrowedBook) {
        assertBorrowedBookAllPropertiesEquals(expectedBorrowedBook, getPersistedBorrowedBook(expectedBorrowedBook));
    }

    protected void assertPersistedBorrowedBookToMatchUpdatableProperties(BorrowedBook expectedBorrowedBook) {
        assertBorrowedBookAllUpdatablePropertiesEquals(expectedBorrowedBook, getPersistedBorrowedBook(expectedBorrowedBook));
    }
}
