package com.biman.job_queue.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biman.job_queue.entity.Job;

public interface JobRepository extends JpaRepository<Job, UUID> {}
