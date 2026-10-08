package com.biman.job_queue.worker;

import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.biman.job_queue.entity.Job;
import com.biman.job_queue.service.JobService;

// This component/class responsability is to 
// fetch stale processing jobs. So jobs that are 
// not processed due to a crash or failure during
// the processing phase of a Worker. Fetch them 
// and set them as PENDING and set them back to 
// the queue for other Workers to process them 
// if their attempt number to process them is 
// less than some max attempts.
@Component 
public class RecoveryWorker {

    private JobService jobService;

    public RecoveryWorker(
        JobService jobService
    ) {
        this.jobService = jobService;
    }

    @Scheduled(fixedDelay = 5000)
    public void recoverJob() {

        Optional<Job> staleJob = jobService.recoverNextStaleJob();

        if (staleJob.isEmpty()) {
            // System.out.println(
            //     "Aucun job à récupérer"
            // );
            return;
        }

        // System.out.println(
        //     "Job récupéré : " + staleJob.get().getId()
        // );
    }
}
