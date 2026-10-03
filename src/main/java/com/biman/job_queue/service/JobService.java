package com.biman.job_queue.service;

import java.util.List;
import java.util.UUID;


import org.springframework.stereotype.Service;

import com.biman.job_queue.entity.Job;
import com.biman.job_queue.repository.JobRepository;

@Service 
public class JobService {
    
    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Job getJobById(UUID id) {
        Job job = jobRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found job wiith ID : " + id));

        return job;
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }
}
