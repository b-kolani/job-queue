package com.biman.job_queue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.biman.job_queue.entity.Job;
import com.biman.job_queue.entity.JobStatus;

public class JobTest {
    
    @Test 
    void createJobTest() {

        Map<String, Object> payload = new HashMap<>();
        payload.put("to", "test@example.com");

        Job job = new Job("EMAIL", payload);

        // Test if a new job status equals to PENDING
        assertEquals(JobStatus.PENDING, job.getStatus());

        // Test if a new job attempts number equals to 0
        assertEquals(0, job.getAttempts());
    }

    @Test 
    void startProcessingTest() {

        Map<String, Object> payload = new HashMap<>();
        payload.put("to", "test@example.com");

        Job job = new Job("EMAIL", payload);

        job.startProcessing();

        // Test if the job status becomes processing
        // after starting processing
        assertEquals(JobStatus.PROCESSING, job.getStatus());

        // Test if a processing job startedAt is not null
        assertNotNull(job.getStartedAt());
    }
    
    @Test 
    void completeTest() {

        Map<String, Object> payload = new HashMap<>();
        payload.put("to", "test@example.com");

        Job job = new Job("EMAIL", payload);

        // Start processing the job
        job.startProcessing();

        // Mark the job as completed
        job.complete();

        // Test if job status becomes completed 
        // after processing
        assertEquals(JobStatus.COMPLETED, job.getStatus());

        // If so test if the completedAt is not null
        assertNotNull(job.getCompletedAt());
    }

    @Test
    void retryTest() {

        Map<String, Object> payload = new HashMap<>();
        payload.put("to", "test@example.com");

        Job job = new Job("EMAIL", payload);
        job.startProcessing();

        // Retry the job
        job.retry();

        // Test if the job status becomes pending
        // after a retry
        assertEquals(JobStatus.PENDING, job.getStatus());

        // Test if attempts equals to 1
        assertEquals(1, job.getAttempts());

        // Test if startedAt is null
        assertNull(job.getStartedAt());
    }

    @Test 
    void canRetryTest() {

        Map<String, Object> payload = new HashMap<>();
        payload.put("to", "test@example.com");

        Job job = new Job("EMAIL", payload);

        job.startProcessing();
        assertTrue(job.canRetry());
        assertEquals(0, job.getAttempts());
        job.retry();

        job.startProcessing();
        assertTrue(job.canRetry());
        assertEquals(1, job.getAttempts());
        job.retry();

        job.startProcessing();
        assertTrue(job.canRetry());
        assertEquals(2, job.getAttempts());
        job.retry();

        // Test if canRetry returns false when attempts = 3
        assertFalse(job.canRetry());
        assertEquals(3, job.getAttempts());
    }

    @Test
    void failAfterMaxRetriesTest() {

        Map<String, Object> payload = new HashMap<>();
        payload.put("to", "test@example.com");

        Job job = new Job("EMAIL", payload);
        job.startProcessing();
       
        // Make the processing of the job fails
        job.fail();

        // Test if status becomes failed
        assertEquals(JobStatus.FAILED, job.getStatus());

        // Test if the completedAt is null 
        assertNull(job.getCompletedAt());

    }
}
