package com.biman.job_queue.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biman.job_queue.dto.CreateJobRequest;
import com.biman.job_queue.entity.Job;
import com.biman.job_queue.exception.JobNotFoundException;
import com.biman.job_queue.repository.JobRepository;

@Service 
public class JobService {
    
    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Job getJobById(UUID id) {
        return jobRepository.findById(id)
            .orElseThrow(() -> new JobNotFoundException("Job not found."));
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    // Business logic
    public Job createJob(CreateJobRequest request) {
        Job job = new Job(
            request.getType(), 
            request.getPayload());

        return jobRepository.save(job);
    }

    // Transactional Method used by a Worker to reserve a Job
    @Transactional 
    public Optional<Job> reserveNextJob() {
        Optional<Job> job = jobRepository.findNextPendingJob();

        if (job.isEmpty()) {
            return Optional.empty();
        }

        Job pendingJob = job.get();

        // We don't want let the Service aknowledge 
        // the internal details of the transaction
        // ie how the job passes from PENDING to PROCESSING.
        // This system is one caracteristic of a state machine
        // (transition)
        pendingJob.startProcessing();

        jobRepository.save(pendingJob);

        return Optional.of(pendingJob);
    }

    public Job completeJob(Job job) {
        job.complete();

        // We can remove the line below and let JPA
        // do the Dirty Checking
        jobRepository.save(job);

        return job;
    }

    // Transactional Method for RecoveryWorker 
    // to recover a stale processing job
    @Transactional
    public Optional<Job> recoverNextStaleJob() {

        Optional<Job> staleJob = jobRepository.findNextStaleProcessingJob();

        if (staleJob.isEmpty()) {
            return Optional.empty();
        }

        Job recoveredJob = staleJob.get();

        if (recoveredJob.canRetry()) {
            // recoveredJob.setStatus(JobStatus.PENDING);
            // recoveredJob.setStartedAt(null);
            recoveredJob.retry();
            // recoveredJob.setAttempts(attempts + 1);
        } else {
            // recoveredJob.setStatus(JobStatus.FAILED);
            recoveredJob.fail();
        }
        
        // This can be skip beacuse JPA will auto-detected changes 
        // and save or update data in the database (Dirty Checking of JPA)
        // jobRepository.save(recoveredJob);

        return Optional.of(recoveredJob);
    }
}
