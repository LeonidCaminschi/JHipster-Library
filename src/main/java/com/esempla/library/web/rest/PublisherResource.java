package com.esempla.library.web.rest;

import com.esempla.library.domain.Publisher;
import com.esempla.library.repository.PublisherRepository;
import com.esempla.library.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.esempla.library.domain.Publisher}.
 */
@RestController
@RequestMapping("/api/publishers")
@Transactional
public class PublisherResource {

    private static final Logger LOG = LoggerFactory.getLogger(PublisherResource.class);

    private static final String ENTITY_NAME = "publisher";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PublisherRepository publisherRepository;

    public PublisherResource(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    /**
     * {@code POST  /publishers} : Create a new publisher.
     *
     * @param publisher the publisher to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new publisher, or with status {@code 400 (Bad Request)} if the publisher has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Publisher> createPublisher(@Valid @RequestBody Publisher publisher) throws URISyntaxException {
        LOG.debug("REST request to save Publisher : {}", publisher);
        if (publisher.getId() != null) {
            throw new BadRequestAlertException("A new publisher cannot already have an ID", ENTITY_NAME, "idexists");
        }
        publisher = publisherRepository.save(publisher);
        return ResponseEntity.created(new URI("/api/publishers/" + publisher.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, publisher.getId().toString()))
            .body(publisher);
    }

    /**
     * {@code PUT  /publishers/:id} : Updates an existing publisher.
     *
     * @param id the id of the publisher to save.
     * @param publisher the publisher to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated publisher,
     * or with status {@code 400 (Bad Request)} if the publisher is not valid,
     * or with status {@code 500 (Internal Server Error)} if the publisher couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Publisher> updatePublisher(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Publisher publisher
    ) throws URISyntaxException {
        LOG.debug("REST request to update Publisher : {}, {}", id, publisher);
        if (publisher.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, publisher.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!publisherRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        publisher = publisherRepository.save(publisher);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, publisher.getId().toString()))
            .body(publisher);
    }

    /**
     * {@code GET  /publishers} : get all the publishers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of publishers in body.
     */
    @GetMapping("")
    public List<Publisher> getAllPublishers() {
        LOG.debug("REST request to get all Publishers");
        return publisherRepository.findAll();
    }

    /**
     * {@code GET  /publishers/:id} : get the "id" publisher.
     *
     * @param id the id of the publisher to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the publisher, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Publisher> getPublisher(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Publisher : {}", id);
        Optional<Publisher> publisher = publisherRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(publisher);
    }

    /**
     * {@code DELETE  /publishers/:id} : delete the "id" publisher.
     *
     * @param id the id of the publisher to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Publisher : {}", id);
        publisherRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
