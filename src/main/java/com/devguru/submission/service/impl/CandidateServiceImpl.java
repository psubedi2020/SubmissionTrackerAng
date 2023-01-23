package com.devguru.submission.service.impl;

import com.devguru.submission.domain.Candidate;
import com.devguru.submission.repository.CandidateRepository;
import com.devguru.submission.service.CandidateService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Candidate}.
 */
@Service
@Transactional
public class CandidateServiceImpl implements CandidateService {

    private final Logger log = LoggerFactory.getLogger(CandidateServiceImpl.class);

    private final CandidateRepository candidateRepository;

    public CandidateServiceImpl(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    @Override
    public Candidate save(Candidate candidate) {
        log.debug("Request to save Candidate : {}", candidate);
        return candidateRepository.save(candidate);
    }

    @Override
    public Candidate update(Candidate candidate) {
        log.debug("Request to update Candidate : {}", candidate);
        return candidateRepository.save(candidate);
    }

    @Override
    public Optional<Candidate> partialUpdate(Candidate candidate) {
        log.debug("Request to partially update Candidate : {}", candidate);

        return candidateRepository
            .findById(candidate.getId())
            .map(existingCandidate -> {
                if (candidate.getFirstName() != null) {
                    existingCandidate.setFirstName(candidate.getFirstName());
                }
                if (candidate.getLastName() != null) {
                    existingCandidate.setLastName(candidate.getLastName());
                }
                if (candidate.getEmail() != null) {
                    existingCandidate.setEmail(candidate.getEmail());
                }
                if (candidate.getPhoneNumber() != null) {
                    existingCandidate.setPhoneNumber(candidate.getPhoneNumber());
                }
                if (candidate.getAvailableDate() != null) {
                    existingCandidate.setAvailableDate(candidate.getAvailableDate());
                }
                if (candidate.getExpectedSalary() != null) {
                    existingCandidate.setExpectedSalary(candidate.getExpectedSalary());
                }

                return existingCandidate;
            })
            .map(candidateRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Candidate> findAll(Pageable pageable) {
        log.debug("Request to get all Candidates");
        return candidateRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Candidate> findOne(Long id) {
        log.debug("Request to get Candidate : {}", id);
        return candidateRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Candidate : {}", id);
        candidateRepository.deleteById(id);
    }
}
