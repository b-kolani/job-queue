package com.biman.job_queue.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.biman.job_queue.dto.CreateJobRequest;
import com.biman.job_queue.entity.Job;
import com.biman.job_queue.service.JobService;


@RestController 
public class JobController {

    private JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // ---- REQUESTS HANDLERS ----
    @PostMapping("/jobs")
    public Job createJob(@RequestBody CreateJobRequest request) {
        return jobService.createJob(request);
    }

    @PostMapping("/jobs/reserve")
    public Optional<Job> reserveJob() {
        return jobService.reserveNextJob();
    }
    
}
