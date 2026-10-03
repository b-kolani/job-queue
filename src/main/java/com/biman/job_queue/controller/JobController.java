package com.biman.job_queue.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biman.job_queue.dto.CreateJobRequest;
import com.biman.job_queue.entity.Job;
import com.biman.job_queue.service.JobService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

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
    
}
