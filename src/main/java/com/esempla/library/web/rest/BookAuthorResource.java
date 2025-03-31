package com.esempla.library.web.rest;

import com.esempla.library.domain.BookAuthor;
import com.esempla.library.repository.BookAuthorRepository;
import com.esempla.library.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.esempla.library.domain.BookAuthor}.
 */
@RestController
@RequestMapping("/api/book-authors")
@Transactional
public class BookAuthorResource {

    private static final Logger LOG = LoggerFactory.getLogger(BookAuthorResource.class);

    private static final String ENTITY_NAME = "bookAuthor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookAuthorRepository bookAuthorRepository;

    public BookAuthorResource(BookAuthorRepository bookAuthorRepository) {
        this.bookAuthorRepository = bookAuthorRepository;
    }

    /**
     * {@code POST  /book-authors} : Create a new bookAuthor.
     *
     * @param bookAuthor the bookAuthor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookAuthor, or with status {@code 400 (Bad Request)} if the bookAuthor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BookAuthor> createBookAuthor(@RequestBody BookAuthor bookAuthor) throws URISyntaxException {
        LOG.debug("REST request to save BookAuthor : {}", bookAuthor);
        if (bookAuthor.getId() != null) {
            throw new BadRequestAlertException("A new bookAuthor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bookAuthor = bookAuthorRepository.save(bookAuthor);
        return ResponseEntity.created(new URI("/api/book-authors/" + bookAuthor.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, bookAuthor.getId().toString()))
            .body(bookAuthor);
    }

    /**
     * {@code PUT  /book-authors/:id} : Updates an existing bookAuthor.
     *
     * @param id the id of the bookAuthor to save.
     * @param bookAuthor the bookAuthor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookAuthor,
     * or with status {@code 400 (Bad Request)} if the bookAuthor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookAuthor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookAuthor> updateBookAuthor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BookAuthor bookAuthor
    ) throws URISyntaxException {
        LOG.debug("REST request to update BookAuthor : {}, {}", id, bookAuthor);
        if (bookAuthor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookAuthor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookAuthorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bookAuthor = bookAuthorRepository.save(bookAuthor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookAuthor.getId().toString()))
            .body(bookAuthor);
    }

    /**
     * {@code PATCH  /book-authors/:id} : Partial updates given fields of an existing bookAuthor, field will ignore if it is null
     *
     * @param id the id of the bookAuthor to save.
     * @param bookAuthor the bookAuthor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookAuthor,
     * or with status {@code 400 (Bad Request)} if the bookAuthor is not valid,
     * or with status {@code 404 (Not Found)} if the bookAuthor is not found,
     * or with status {@code 500 (Internal Server Error)} if the bookAuthor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BookAuthor> partialUpdateBookAuthor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BookAuthor bookAuthor
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BookAuthor partially : {}, {}", id, bookAuthor);
        if (bookAuthor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookAuthor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookAuthorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BookAuthor> result = bookAuthorRepository.findById(bookAuthor.getId()).map(bookAuthorRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookAuthor.getId().toString())
        );
    }

    /**
     * {@code GET  /book-authors} : get all the bookAuthors.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookAuthors in body.
     */
    @GetMapping("")
    public List<BookAuthor> getAllBookAuthors() {
        LOG.debug("REST request to get all BookAuthors");
        return bookAuthorRepository.findAll();
    }

    /**
     * {@code GET  /book-authors/:id} : get the "id" bookAuthor.
     *
     * @param id the id of the bookAuthor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookAuthor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookAuthor> getBookAuthor(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BookAuthor : {}", id);
        Optional<BookAuthor> bookAuthor = bookAuthorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(bookAuthor);
    }

    /**
     * {@code DELETE  /book-authors/:id} : delete the "id" bookAuthor.
     *
     * @param id the id of the bookAuthor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookAuthor(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BookAuthor : {}", id);
        bookAuthorRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
