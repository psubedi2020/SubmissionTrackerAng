package com.devguru.submission.web.rest;

import static com.devguru.submission.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devguru.submission.IntegrationTest;
import com.devguru.submission.domain.Submission;
import com.devguru.submission.domain.enumeration.SubmissionStatus;
import com.devguru.submission.repository.SubmissionRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SubmissionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubmissionResourceIT {

    private static final String DEFAULT_JOB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_JOB_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_JOB_URL = "AAAAAAAAAA";
    private static final String UPDATED_JOB_URL = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_QUOTED_SALARY = new BigDecimal(10000);
    private static final BigDecimal UPDATED_QUOTED_SALARY = new BigDecimal(10001);

    private static final String DEFAULT_JOB_REQUISITION_ID = "AAAAAAAAAA";
    private static final String UPDATED_JOB_REQUISITION_ID = "BBBBBBBBBB";

    private static final Instant DEFAULT_APPLICATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_APPLICATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final SubmissionStatus DEFAULT_SUBMISSION_STATUS = SubmissionStatus.SUBMITTED;
    private static final SubmissionStatus UPDATED_SUBMISSION_STATUS = SubmissionStatus.PRESCREENED;

    private static final String ENTITY_API_URL = "/api/submissions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubmissionMockMvc;

    private Submission submission;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Submission createEntity(EntityManager em) {
        Submission submission = new Submission()
            .jobTitle(DEFAULT_JOB_TITLE)
            .companyName(DEFAULT_COMPANY_NAME)
            .jobURL(DEFAULT_JOB_URL)
            .quotedSalary(DEFAULT_QUOTED_SALARY)
            .jobRequisitionId(DEFAULT_JOB_REQUISITION_ID)
            .applicationDate(DEFAULT_APPLICATION_DATE)
            .submissionStatus(DEFAULT_SUBMISSION_STATUS);
        return submission;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Submission createUpdatedEntity(EntityManager em) {
        Submission submission = new Submission()
            .jobTitle(UPDATED_JOB_TITLE)
            .companyName(UPDATED_COMPANY_NAME)
            .jobURL(UPDATED_JOB_URL)
            .quotedSalary(UPDATED_QUOTED_SALARY)
            .jobRequisitionId(UPDATED_JOB_REQUISITION_ID)
            .applicationDate(UPDATED_APPLICATION_DATE)
            .submissionStatus(UPDATED_SUBMISSION_STATUS);
        return submission;
    }

    @BeforeEach
    public void initTest() {
        submission = createEntity(em);
    }

    @Test
    @Transactional
    void createSubmission() throws Exception {
        int databaseSizeBeforeCreate = submissionRepository.findAll().size();
        // Create the Submission
        restSubmissionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(submission))
            )
            .andExpect(status().isCreated());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeCreate + 1);
        Submission testSubmission = submissionList.get(submissionList.size() - 1);
        assertThat(testSubmission.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
        assertThat(testSubmission.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testSubmission.getJobURL()).isEqualTo(DEFAULT_JOB_URL);
        assertThat(testSubmission.getQuotedSalary()).isEqualByComparingTo(DEFAULT_QUOTED_SALARY);
        assertThat(testSubmission.getJobRequisitionId()).isEqualTo(DEFAULT_JOB_REQUISITION_ID);
        assertThat(testSubmission.getApplicationDate()).isEqualTo(DEFAULT_APPLICATION_DATE);
        assertThat(testSubmission.getSubmissionStatus()).isEqualTo(DEFAULT_SUBMISSION_STATUS);
    }

    @Test
    @Transactional
    void createSubmissionWithExistingId() throws Exception {
        // Create the Submission with an existing ID
        submission.setId(1L);

        int databaseSizeBeforeCreate = submissionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubmissionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(submission))
            )
            .andExpect(status().isBadRequest());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCompanyNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = submissionRepository.findAll().size();
        // set the field null
        submission.setCompanyName(null);

        // Create the Submission, which fails.

        restSubmissionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(submission))
            )
            .andExpect(status().isBadRequest());

        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSubmissions() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList
        restSubmissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(submission.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].jobURL").value(hasItem(DEFAULT_JOB_URL)))
            .andExpect(jsonPath("$.[*].quotedSalary").value(hasItem(sameNumber(DEFAULT_QUOTED_SALARY))))
            .andExpect(jsonPath("$.[*].jobRequisitionId").value(hasItem(DEFAULT_JOB_REQUISITION_ID)))
            .andExpect(jsonPath("$.[*].applicationDate").value(hasItem(DEFAULT_APPLICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].submissionStatus").value(hasItem(DEFAULT_SUBMISSION_STATUS.toString())));
    }

    @Test
    @Transactional
    void getSubmission() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get the submission
        restSubmissionMockMvc
            .perform(get(ENTITY_API_URL_ID, submission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(submission.getId().intValue()))
            .andExpect(jsonPath("$.jobTitle").value(DEFAULT_JOB_TITLE))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME))
            .andExpect(jsonPath("$.jobURL").value(DEFAULT_JOB_URL))
            .andExpect(jsonPath("$.quotedSalary").value(sameNumber(DEFAULT_QUOTED_SALARY)))
            .andExpect(jsonPath("$.jobRequisitionId").value(DEFAULT_JOB_REQUISITION_ID))
            .andExpect(jsonPath("$.applicationDate").value(DEFAULT_APPLICATION_DATE.toString()))
            .andExpect(jsonPath("$.submissionStatus").value(DEFAULT_SUBMISSION_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSubmission() throws Exception {
        // Get the submission
        restSubmissionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubmission() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        int databaseSizeBeforeUpdate = submissionRepository.findAll().size();

        // Update the submission
        Submission updatedSubmission = submissionRepository.findById(submission.getId()).get();
        // Disconnect from session so that the updates on updatedSubmission are not directly saved in db
        em.detach(updatedSubmission);
        updatedSubmission
            .jobTitle(UPDATED_JOB_TITLE)
            .companyName(UPDATED_COMPANY_NAME)
            .jobURL(UPDATED_JOB_URL)
            .quotedSalary(UPDATED_QUOTED_SALARY)
            .jobRequisitionId(UPDATED_JOB_REQUISITION_ID)
            .applicationDate(UPDATED_APPLICATION_DATE)
            .submissionStatus(UPDATED_SUBMISSION_STATUS);

        restSubmissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSubmission.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSubmission))
            )
            .andExpect(status().isOk());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeUpdate);
        Submission testSubmission = submissionList.get(submissionList.size() - 1);
        assertThat(testSubmission.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testSubmission.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testSubmission.getJobURL()).isEqualTo(UPDATED_JOB_URL);
        assertThat(testSubmission.getQuotedSalary()).isEqualByComparingTo(UPDATED_QUOTED_SALARY);
        assertThat(testSubmission.getJobRequisitionId()).isEqualTo(UPDATED_JOB_REQUISITION_ID);
        assertThat(testSubmission.getApplicationDate()).isEqualTo(UPDATED_APPLICATION_DATE);
        assertThat(testSubmission.getSubmissionStatus()).isEqualTo(UPDATED_SUBMISSION_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingSubmission() throws Exception {
        int databaseSizeBeforeUpdate = submissionRepository.findAll().size();
        submission.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubmissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, submission.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(submission))
            )
            .andExpect(status().isBadRequest());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubmission() throws Exception {
        int databaseSizeBeforeUpdate = submissionRepository.findAll().size();
        submission.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubmissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(submission))
            )
            .andExpect(status().isBadRequest());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubmission() throws Exception {
        int databaseSizeBeforeUpdate = submissionRepository.findAll().size();
        submission.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubmissionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(submission))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubmissionWithPatch() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        int databaseSizeBeforeUpdate = submissionRepository.findAll().size();

        // Update the submission using partial update
        Submission partialUpdatedSubmission = new Submission();
        partialUpdatedSubmission.setId(submission.getId());

        partialUpdatedSubmission.jobTitle(UPDATED_JOB_TITLE).companyName(UPDATED_COMPANY_NAME);

        restSubmissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubmission.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubmission))
            )
            .andExpect(status().isOk());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeUpdate);
        Submission testSubmission = submissionList.get(submissionList.size() - 1);
        assertThat(testSubmission.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testSubmission.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testSubmission.getJobURL()).isEqualTo(DEFAULT_JOB_URL);
        assertThat(testSubmission.getQuotedSalary()).isEqualByComparingTo(DEFAULT_QUOTED_SALARY);
        assertThat(testSubmission.getJobRequisitionId()).isEqualTo(DEFAULT_JOB_REQUISITION_ID);
        assertThat(testSubmission.getApplicationDate()).isEqualTo(DEFAULT_APPLICATION_DATE);
        assertThat(testSubmission.getSubmissionStatus()).isEqualTo(DEFAULT_SUBMISSION_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateSubmissionWithPatch() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        int databaseSizeBeforeUpdate = submissionRepository.findAll().size();

        // Update the submission using partial update
        Submission partialUpdatedSubmission = new Submission();
        partialUpdatedSubmission.setId(submission.getId());

        partialUpdatedSubmission
            .jobTitle(UPDATED_JOB_TITLE)
            .companyName(UPDATED_COMPANY_NAME)
            .jobURL(UPDATED_JOB_URL)
            .quotedSalary(UPDATED_QUOTED_SALARY)
            .jobRequisitionId(UPDATED_JOB_REQUISITION_ID)
            .applicationDate(UPDATED_APPLICATION_DATE)
            .submissionStatus(UPDATED_SUBMISSION_STATUS);

        restSubmissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubmission.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubmission))
            )
            .andExpect(status().isOk());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeUpdate);
        Submission testSubmission = submissionList.get(submissionList.size() - 1);
        assertThat(testSubmission.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testSubmission.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testSubmission.getJobURL()).isEqualTo(UPDATED_JOB_URL);
        assertThat(testSubmission.getQuotedSalary()).isEqualByComparingTo(UPDATED_QUOTED_SALARY);
        assertThat(testSubmission.getJobRequisitionId()).isEqualTo(UPDATED_JOB_REQUISITION_ID);
        assertThat(testSubmission.getApplicationDate()).isEqualTo(UPDATED_APPLICATION_DATE);
        assertThat(testSubmission.getSubmissionStatus()).isEqualTo(UPDATED_SUBMISSION_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingSubmission() throws Exception {
        int databaseSizeBeforeUpdate = submissionRepository.findAll().size();
        submission.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubmissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, submission.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(submission))
            )
            .andExpect(status().isBadRequest());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubmission() throws Exception {
        int databaseSizeBeforeUpdate = submissionRepository.findAll().size();
        submission.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubmissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(submission))
            )
            .andExpect(status().isBadRequest());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubmission() throws Exception {
        int databaseSizeBeforeUpdate = submissionRepository.findAll().size();
        submission.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubmissionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(submission))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubmission() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        int databaseSizeBeforeDelete = submissionRepository.findAll().size();

        // Delete the submission
        restSubmissionMockMvc
            .perform(delete(ENTITY_API_URL_ID, submission.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
