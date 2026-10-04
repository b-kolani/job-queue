package com.biman.job_queue.worker;

import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.biman.job_queue.entity.Job;
import com.biman.job_queue.service.JobService;

@Component 
public class JobWorker {
    private JobService jobService;

    public JobWorker(JobService jobService) {
        this.jobService = jobService;
    }


    // Ask to the Service there is a job 
    // to process
    @Scheduled(fixedDelay = 1000)
    public void processNextJob() {
        Optional<Job> job = jobService.reserveNextJob();

        // If there is no job the Worker exits
        if (job.isEmpty()) {
            return;
        }

        // Fetch the reserved job
        Job pendingJob = job.get();

        // Simulate the execution of the job
        // using a timer
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        jobService.completeJob(pendingJob);
    }
}
