package com.biman.job_queue.worker;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.biman.job_queue.entity.Job;
import com.biman.job_queue.service.JobService;

//This component/class responsability is to 
// process jobs from the PENDING phase to 
// COMPLETED or FAILED phase.
@Component 
public class JobWorker {
    private JobService jobService;

    public JobWorker(JobService jobService) {
        this.jobService = jobService;
    }


    // Ask to the Service there is a job 
    // to process
    public void processNextJob() {
        System.out.println(
            "Worker Thread: " + Thread.currentThread().getName()
        );

        Optional<Job> job = jobService.reserveNextJob();

        // If there is no job the Worker exits
        if (job.isEmpty()) {
            return;
        }

        // Fetch the reserved job
        Job pendingJob = job.get();

        System.out.println(
            "Worker " + Thread.currentThread().getName()
            + " traite le job " + pendingJob.getId()
        );

        // Simulate the execution of the job
        // using a timer
        System.out.println(
            "START job " + pendingJob.getId()
            + " - " + Thread.currentThread().getName()
        );

        // This line is added volontary 
        // to simulate a crash in order to test if 
        // a stale processing job is recovered or not
        // throw new RuntimeException("Simulation d'un crash du worker");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        System.out.println(
            "END job " + pendingJob.getId()
            + " - " + Thread.currentThread().getName()
        );

        jobService.completeJob(pendingJob);
    }
}
