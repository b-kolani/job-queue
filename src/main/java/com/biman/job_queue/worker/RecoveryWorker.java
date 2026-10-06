package com.biman.job_queue.worker;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.biman.job_queue.entity.Job;
import com.biman.job_queue.repository.JobRepository;

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

    private JobRepository jobRepository;

    public RecoveryWorker(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }
    
    @Scheduled(fixedDelay = 5000)
    public void recoverJobs() {

        List<Job> staleJobs = jobRepository.findStaleProcessingJobs();

        for (Job job : staleJobs) {
            System.out.println(
                "Job abandonné détecté : " + job.getId()
            );
        }
    }
}
