package com.biman.job_queue.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.biman.job_queue.entity.Job;

public interface JobRepository extends JpaRepository<Job, UUID> {

    @Query(value = """
        SELECT *
        FROM jobs
        WHERE status = 'PENDING'
        ORDER BY created_at ASC
        LIMIT 1
        FOR UPDATE SKIP LOCKED
        """, nativeQuery = true)
    Optional<Job> findNextPendingJob();

    @Query(value = """
        SELECT * 
        FROM jobs
        WHERE status = 'PROCESSING'
            AND started_at < NOW() - INTERVAL '15 seconds'
        """, nativeQuery = true)
    List<Job> findStaleProcessingJobs();

    @Query(value = """
        SELECT * 
        FROM jobs 
        WHERE status = 'PROCESSING'
            AND started_at < NOW() - INTERVAL '15 seconds'
        ORDER BY started_at ASC
        LIMIT 1
        FOR UPDATE SKIP LOCKED; 
        """, nativeQuery = true)
    Optional<Job> findNextStaleProcessingJob();
}
