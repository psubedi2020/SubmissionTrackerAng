package com.devguru.submission.web.rest;

import com.devguru.submission.domain.Submission;
import com.devguru.submission.repository.SubmissionRepository;
import com.devguru.submission.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.devguru.submission.domain.Submission}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SubmissionResource {

    private final Logger log = LoggerFactory.getLogger(SubmissionResource.class);

    private static final String ENTITY_NAME = "submission";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubmissionRepository submissionRepository;

    public SubmissionResource(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    /**
     * {@code POST  /submissions} : Create a new submission.
     *
     * @param submission the submission to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new submission, or with status {@code 400 (Bad Request)} if the submission has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/submissions")
    public ResponseEntity<Submission> createSubmission(@Valid @RequestBody Submission submission) throws URISyntaxException {
        log.debug("REST request to save Submission : {}", submission);
        if (submission.getId() != null) {
            throw new BadRequestAlertException("A new submission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Submission result = submissionRepository.save(submission);
        return ResponseEntity
            .created(new URI("/api/submissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /submissions/:id} : Updates an existing submission.
     *
     * @param id the id of the submission to save.
     * @param submission the submission to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated submission,
     * or with status {@code 400 (Bad Request)} if the submission is not valid,
     * or with status {@code 500 (Internal Server Error)} if the submission couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/submissions/{id}")
    public ResponseEntity<Submission> updateSubmission(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Submission submission
    ) throws URISyntaxException {
        log.debug("REST request to update Submission : {}, {}", id, submission);
        if (submission.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, submission.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!submissionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Submission result = submissionRepository.save(submission);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, submission.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /submissions/:id} : Partial updates given fields of an existing submission, field will ignore if it is null
     *
     * @param id the id of the submission to save.
     * @param submission the submission to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated submission,
     * or with status {@code 400 (Bad Request)} if the submission is not valid,
     * or with status {@code 404 (Not Found)} if the submission is not found,
     * or with status {@code 500 (Internal Server Error)} if the submission couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/submissions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Submission> partialUpdateSubmission(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Submission submission
    ) throws URISyntaxException {
        log.debug("REST request to partial update Submission partially : {}, {}", id, submission);
        if (submission.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, submission.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!submissionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Submission> result = submissionRepository
            .findById(submission.getId())
            .map(existingSubmission -> {
                if (submission.getJobTitle() != null) {
                    existingSubmission.setJobTitle(submission.getJobTitle());
                }
                if (submission.getCompanyName() != null) {
                    existingSubmission.setCompanyName(submission.getCompanyName());
                }
                if (submission.getJobURL() != null) {
                    existingSubmission.setJobURL(submission.getJobURL());
                }
                if (submission.getQuotedSalary() != null) {
                    existingSubmission.setQuotedSalary(submission.getQuotedSalary());
                }
                if (submission.getJobRequisitionId() != null) {
                    existingSubmission.setJobRequisitionId(submission.getJobRequisitionId());
                }
                if (submission.getApplicationDate() != null) {
                    existingSubmission.setApplicationDate(submission.getApplicationDate());
                }
                if (submission.getSubmissionStatus() != null) {
                    existingSubmission.setSubmissionStatus(submission.getSubmissionStatus());
                }

                return existingSubmission;
            })
            .map(submissionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, submission.getId().toString())
        );
    }

    /**
     * {@code GET  /submissions} : get all the submissions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of submissions in body.
     */
    @GetMapping("/submissions")
    public ResponseEntity<List<Submission>> getAllSubmissions(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Submissions");
        Page<Submission> page = submissionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /submissions/:id} : get the "id" submission.
     *
     * @param id the id of the submission to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the submission, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/submissions/{id}")
    public ResponseEntity<Submission> getSubmission(@PathVariable Long id) {
        log.debug("REST request to get Submission : {}", id);
        Optional<Submission> submission = submissionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(submission);
    }

    /**
     * {@code DELETE  /submissions/:id} : delete the "id" submission.
     *
     * @param id the id of the submission to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/submissions/{id}")
    public ResponseEntity<Void> deleteSubmission(@PathVariable Long id) {
        log.debug("REST request to delete Submission : {}", id);
        submissionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
