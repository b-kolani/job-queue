package com.biman.job_queue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.biman.job_queue.dto.CreateJobRequest;
import com.biman.job_queue.entity.Job;
import com.biman.job_queue.entity.JobStatus;
import com.biman.job_queue.repository.JobRepository;
import com.biman.job_queue.service.JobService;

// This property disabled WorkerExecutor, RecovoryWorker
// in Spring Context managed and run automatically by Spring
// based on the condition we added.
@SpringBootTest(properties = {
    "job.worker.enabled=false",
    "job.recovery.enabled=false"
})
public class JobServiceTest {
    
    @Autowired 
    private JobService jobService;

    @Autowired 
    private JobRepository jobRepository;

    @Test
    void reserveNextJobTest() {


        // Why deleteAll() ?
        // 1. Delete old jobs before the test.
        // 2. Create just one new job PENDING.
        // 3. reserveNextJob() won't select an old job PENDING.
        // 4. Verify that the created job becomes PROCESSING
        jobRepository.deleteAll();

        CreateJobRequest request = new CreateJobRequest();
        Map<String, Object> payload = new HashMap<>();

        payload.put("to", "test@example.com");
        request.setType("EMAIL");
        request.setPayload(payload);

        Job job = jobService.createJob(request);
        System.out.println("ID créé par le test : " + job.getId());

        assertNotNull(job);
        assertEquals(JobStatus.PENDING, job.getStatus());
        assertEquals(0, job.getAttempts());
        assertNull(job.getStartedAt());

        Optional<Job> reservedJob = jobService.reserveNextJob();
        assertTrue(reservedJob.isPresent());
        
        Job processingJob = reservedJob.get();
        System.out.println("ID reservé : " + processingJob.getId());
        assertEquals(JobStatus.PROCESSING, processingJob.getStatus());
        assertNotNull(processingJob.getStartedAt());

        Optional<Job> foundJob = jobRepository.findById(job.getId());
        assertTrue(foundJob.isPresent());

        Job foundJobFromDB = foundJob.get();
        assertEquals(JobStatus.PROCESSING, foundJobFromDB.getStatus());
        assertNotNull(foundJobFromDB.getStartedAt());

    }
}
