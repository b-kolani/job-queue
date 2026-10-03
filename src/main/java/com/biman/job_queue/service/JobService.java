package com.biman.job_queue.service;

import java.util.List;
import java.util.UUID;


import org.springframework.stereotype.Service;

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
}
