package com.biman.job_queue.entity;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table(name="jobs")
public class Job {

    @Id 
    @Column(name="id")
    private UUID id = UUID.randomUUID();
    
    @Column(name="type")
    private String type;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name="payload")
    private Map<String, Object> payload;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private JobStatus status = JobStatus.PENDING;

    @Column(name="attempts")
    private int attempts = 0;

    @org.hibernate.annotations.Generated 
    @Column(name="created_at")
    private OffsetDateTime createdAt;

    // Constructor for JPA
    protected Job() {}

    // Parameterized constructor
    public Job(String type, Map<String, Object> payload) {
        this.type = type;
        this.payload = payload;
    }

    // ---- GETTERS ----
    public UUID getId() {
        return id;
    }
    
    public String getType() {
        return type;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public JobStatus getStatus() {
        return status;
    }

    public int getAttempts() {
        return attempts;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    // ---- SETTERS ----
    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public void setAttempts(int attempts) {
        if (attempts < 0) {
            throw new IllegalArgumentException("Attempts cannot be negative.");
        }

        this.attempts = attempts;
    }
}