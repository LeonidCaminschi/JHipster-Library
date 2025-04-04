package com.esempla.library.web.rest;

import static com.esempla.library.config.Constants.*;

import com.esempla.library.domain.BorrowedBook;
import com.esempla.library.repository.BorrowedBookRepository;
import com.esempla.library.service.BorrowedBookService;
import com.esempla.library.service.dto.BorrowReturnRequest;
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
 * REST controller for managing {@link com.esempla.library.domain.BorrowedBook}.
 */
@RestController
@RequestMapping("/api/borrowed-books")
@Transactional
public class BorrowedBookResource {

    private static final Logger LOG = LoggerFactory.getLogger(BorrowedBookResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BorrowedBookService borrowedBookService;

    public BorrowedBookResource(BorrowedBookService borrowedBookService) {
        this.borrowedBookService = borrowedBookService;
    }

    /**
     * {@code POST  /borrowed-books} : Create a new borrowedBook.
     *
     * @param borrowedBook the borrowedBook to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new borrowedBook, or with status {@code 400 (Bad Request)} if the borrowedBook has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BorrowedBook> createBorrowedBook(@Valid @RequestBody BorrowedBook borrowedBook) throws URISyntaxException {
        LOG.debug("REST request to save BorrowedBook : {}", borrowedBook);
        if (borrowedBook.getId() != null) {
            throw new BadRequestAlertException(A_NEW_BORROWED_BOOK_CANNOT_ALREADY_HAVE_AN_ID, ENTITY_NAME, IDEXISTS);
        }
        borrowedBook = borrowedBookService.save(borrowedBook);
        return ResponseEntity.created(new URI("/api/borrowed-books/" + borrowedBook.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, borrowedBook.getId().toString()))
            .body(borrowedBook);
    }

    /**
     * {@code PUT  /borrowed-books/:id} : Updates an existing borrowedBook.
     *
     * @param id the id of the borrowedBook to save.
     * @param borrowedBook the borrowedBook to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated borrowedBook,
     * or with status {@code 400 (Bad Request)} if the borrowedBook is not valid,
     * or with status {@code 500 (Internal Server Error)} if the borrowedBook couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BorrowedBook> updateBorrowedBook(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BorrowedBook borrowedBook
    ) throws URISyntaxException {
        LOG.debug("REST request to update BorrowedBook : {}, {}", id, borrowedBook);
        if (borrowedBook.getId() == null) {
            throw new BadRequestAlertException(INVALID_ID, ENTITY_NAME, IDNULL);
        }
        if (!Objects.equals(id, borrowedBook.getId())) {
            throw new BadRequestAlertException(INVALID_ID, ENTITY_NAME, IDINVALID);
        }

        if (!borrowedBookService.existsById(id)) {
            throw new BadRequestAlertException(ENTITY_NOT_FOUND, ENTITY_NAME, IDNOTFOUND);
        }

        borrowedBook = borrowedBookService.save(borrowedBook);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, borrowedBook.getId().toString()))
            .body(borrowedBook);
    }

    /**
     * {@code GET  /borrowed-books} : get all the borrowedBooks.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of borrowedBooks in body.
     */
    @GetMapping("")
    public List<BorrowedBook> getAllBorrowedBooks() {
        LOG.debug("REST request to get all BorrowedBooks");
        return borrowedBookService.findAll();
    }

    /**
     * {@code GET  /borrowed-books/:id} : get the "id" borrowedBook.
     *
     * @param id the id of the borrowedBook to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the borrowedBook, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BorrowedBook> getBorrowedBook(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BorrowedBook : {}", id);
        Optional<BorrowedBook> borrowedBook = borrowedBookService.findById(id);
        return ResponseUtil.wrapOrNotFound(borrowedBook);
    }

    /**
     * {@code DELETE  /borrowed-books/:id} : delete the "id" borrowedBook.
     *
     * @param id the id of the borrowedBook to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorrowedBook(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BorrowedBook : {}", id);
        borrowedBookService.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code POST  /borrow} : Borrow books for a client.
     *
     * @param borrowRequest the request containing client details and book names.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the number of books borrowed.
     */
    @PostMapping("/borrow")
    public ResponseEntity<String> borrowBooks(@RequestBody BorrowReturnRequest borrowRequest) {
        LOG.debug("REST request to borrow books: {}", borrowRequest);
        int borrowedBooksCount = borrowedBookService.borrowBook(borrowRequest);
        return ResponseEntity.ok().body("Successfully borrowed " + borrowedBooksCount + "/" + borrowRequest.bookNames().size() + " books.");
    }

    /**
     * {@code POST  /return} : Return books for a client.
     *
     * @param returnRequest the request containing client details and book names.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the number of books returned.
     */
    @PostMapping("/return")
    public ResponseEntity<String> returnBooks(@RequestBody BorrowReturnRequest returnRequest) {
        LOG.debug("REST request to return books: {}", returnRequest);
        int returnedBooksCount = borrowedBookService.returnBook(returnRequest);
        return ResponseEntity.ok().body("Successfully returned " + returnedBooksCount + "/" + returnRequest.bookNames().size() + " books.");
    }
}
