package com.devguru.submission.domain;

import com.devguru.submission.domain.enumeration.SubmissionStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Submission.
 */
@Entity
@Table(name = "submission")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Submission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "job_title")
    private String jobTitle;

    @NotNull
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "job_url")
    private String jobURL;

    @DecimalMin(value = "10000")
    @DecimalMax(value = "500000.00")
    @Column(name = "quoted_salary", precision = 21, scale = 2)
    private BigDecimal quotedSalary;

    @Column(name = "job_requisition_id")
    private String jobRequisitionId;

    @Column(name = "application_date")
    private Instant applicationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "submission_status")
    private SubmissionStatus submissionStatus;

    @ManyToOne
    @JsonIgnoreProperties(value = { "submissions" }, allowSetters = true)
    private Candidate candidate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Submission id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobTitle() {
        return this.jobTitle;
    }

    public Submission jobTitle(String jobTitle) {
        this.setJobTitle(jobTitle);
        return this;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public Submission companyName(String companyName) {
        this.setCompanyName(companyName);
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobURL() {
        return this.jobURL;
    }

    public Submission jobURL(String jobURL) {
        this.setJobURL(jobURL);
        return this;
    }

    public void setJobURL(String jobURL) {
        this.jobURL = jobURL;
    }

    public BigDecimal getQuotedSalary() {
        return this.quotedSalary;
    }

    public Submission quotedSalary(BigDecimal quotedSalary) {
        this.setQuotedSalary(quotedSalary);
        return this;
    }

    public void setQuotedSalary(BigDecimal quotedSalary) {
        this.quotedSalary = quotedSalary;
    }

    public String getJobRequisitionId() {
        return this.jobRequisitionId;
    }

    public Submission jobRequisitionId(String jobRequisitionId) {
        this.setJobRequisitionId(jobRequisitionId);
        return this;
    }

    public void setJobRequisitionId(String jobRequisitionId) {
        this.jobRequisitionId = jobRequisitionId;
    }

    public Instant getApplicationDate() {
        return this.applicationDate;
    }

    public Submission applicationDate(Instant applicationDate) {
        this.setApplicationDate(applicationDate);
        return this;
    }

    public void setApplicationDate(Instant applicationDate) {
        this.applicationDate = applicationDate;
    }

    public SubmissionStatus getSubmissionStatus() {
        return this.submissionStatus;
    }

    public Submission submissionStatus(SubmissionStatus submissionStatus) {
        this.setSubmissionStatus(submissionStatus);
        return this;
    }

    public void setSubmissionStatus(SubmissionStatus submissionStatus) {
        this.submissionStatus = submissionStatus;
    }

    public Candidate getCandidate() {
        return this.candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public Submission candidate(Candidate candidate) {
        this.setCandidate(candidate);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Submission)) {
            return false;
        }
        return id != null && id.equals(((Submission) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Submission{" +
            "id=" + getId() +
            ", jobTitle='" + getJobTitle() + "'" +
            ", companyName='" + getCompanyName() + "'" +
            ", jobURL='" + getJobURL() + "'" +
            ", quotedSalary=" + getQuotedSalary() +
            ", jobRequisitionId='" + getJobRequisitionId() + "'" +
            ", applicationDate='" + getApplicationDate() + "'" +
            ", submissionStatus='" + getSubmissionStatus() + "'" +
            "}";
    }
}
