package com.esempla.library.web.rest;

import static com.esempla.library.domain.PublisherAsserts.*;
import static com.esempla.library.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.esempla.library.IntegrationTest;
import com.esempla.library.domain.Publisher;
import com.esempla.library.repository.PublisherRepository;
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
 * Integration tests for the {@link PublisherResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PublisherResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/publishers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPublisherMockMvc;

    private Publisher publisher;

    private Publisher insertedPublisher;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Publisher createEntity() {
        return new Publisher().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Publisher createUpdatedEntity() {
        return new Publisher().name(UPDATED_NAME);
    }

    @BeforeEach
    public void initTest() {
        publisher = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPublisher != null) {
            publisherRepository.delete(insertedPublisher);
            insertedPublisher = null;
        }
    }

    @Test
    @Transactional
    void createPublisher() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Publisher
        var returnedPublisher = om.readValue(
            restPublisherMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(publisher)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Publisher.class
        );

        // Validate the Publisher in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPublisherUpdatableFieldsEquals(returnedPublisher, getPersistedPublisher(returnedPublisher));

        insertedPublisher = returnedPublisher;
    }

    @Test
    @Transactional
    void createPublisherWithExistingId() throws Exception {
        // Create the Publisher with an existing ID
        publisher.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPublisherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(publisher)))
            .andExpect(status().isBadRequest());

        // Validate the Publisher in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        publisher.setName(null);

        // Create the Publisher, which fails.

        restPublisherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(publisher)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPublishers() throws Exception {
        // Initialize the database
        insertedPublisher = publisherRepository.saveAndFlush(publisher);

        // Get all the publisherList
        restPublisherMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(publisher.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getPublisher() throws Exception {
        // Initialize the database
        insertedPublisher = publisherRepository.saveAndFlush(publisher);

        // Get the publisher
        restPublisherMockMvc
            .perform(get(ENTITY_API_URL_ID, publisher.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(publisher.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingPublisher() throws Exception {
        // Get the publisher
        restPublisherMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPublisher() throws Exception {
        // Initialize the database
        insertedPublisher = publisherRepository.saveAndFlush(publisher);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the publisher
        Publisher updatedPublisher = publisherRepository.findById(publisher.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPublisher are not directly saved in db
        em.detach(updatedPublisher);
        updatedPublisher.name(UPDATED_NAME);

        restPublisherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPublisher.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPublisher))
            )
            .andExpect(status().isOk());

        // Validate the Publisher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPublisherToMatchAllProperties(updatedPublisher);
    }

    @Test
    @Transactional
    void putNonExistingPublisher() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        publisher.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPublisherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, publisher.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(publisher))
            )
            .andExpect(status().isBadRequest());

        // Validate the Publisher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPublisher() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        publisher.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPublisherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(publisher))
            )
            .andExpect(status().isBadRequest());

        // Validate the Publisher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPublisher() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        publisher.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPublisherMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(publisher)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Publisher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePublisherWithPatch() throws Exception {
        // Initialize the database
        insertedPublisher = publisherRepository.saveAndFlush(publisher);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the publisher using partial update
        Publisher partialUpdatedPublisher = new Publisher();
        partialUpdatedPublisher.setId(publisher.getId());

        restPublisherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPublisher.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPublisher))
            )
            .andExpect(status().isOk());

        // Validate the Publisher in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPublisherUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPublisher, publisher),
            getPersistedPublisher(publisher)
        );
    }

    @Test
    @Transactional
    void fullUpdatePublisherWithPatch() throws Exception {
        // Initialize the database
        insertedPublisher = publisherRepository.saveAndFlush(publisher);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the publisher using partial update
        Publisher partialUpdatedPublisher = new Publisher();
        partialUpdatedPublisher.setId(publisher.getId());

        partialUpdatedPublisher.name(UPDATED_NAME);

        restPublisherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPublisher.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPublisher))
            )
            .andExpect(status().isOk());

        // Validate the Publisher in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPublisherUpdatableFieldsEquals(partialUpdatedPublisher, getPersistedPublisher(partialUpdatedPublisher));
    }

    @Test
    @Transactional
    void patchNonExistingPublisher() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        publisher.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPublisherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, publisher.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(publisher))
            )
            .andExpect(status().isBadRequest());

        // Validate the Publisher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPublisher() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        publisher.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPublisherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(publisher))
            )
            .andExpect(status().isBadRequest());

        // Validate the Publisher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPublisher() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        publisher.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPublisherMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(publisher)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Publisher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePublisher() throws Exception {
        // Initialize the database
        insertedPublisher = publisherRepository.saveAndFlush(publisher);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the publisher
        restPublisherMockMvc
            .perform(delete(ENTITY_API_URL_ID, publisher.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return publisherRepository.count();
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

    protected Publisher getPersistedPublisher(Publisher publisher) {
        return publisherRepository.findById(publisher.getId()).orElseThrow();
    }

    protected void assertPersistedPublisherToMatchAllProperties(Publisher expectedPublisher) {
        assertPublisherAllPropertiesEquals(expectedPublisher, getPersistedPublisher(expectedPublisher));
    }

    protected void assertPersistedPublisherToMatchUpdatableProperties(Publisher expectedPublisher) {
        assertPublisherAllUpdatablePropertiesEquals(expectedPublisher, getPersistedPublisher(expectedPublisher));
    }
}
