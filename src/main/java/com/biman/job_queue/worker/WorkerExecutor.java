package com.biman.job_queue.worker;

import java.time.Duration;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component 
public class WorkerExecutor {
    
    private JobWorker jobWorker;
    private ThreadPoolTaskScheduler scheduler;

    public WorkerExecutor(
        JobWorker jobWorker,
        ThreadPoolTaskScheduler scheduler
    ) {
        this.jobWorker = jobWorker;
        this.scheduler = scheduler;
    }

    @PostConstruct 
    // Schedule 3 threads to execute tasks
    // concurrentely
    public void start() {
        // Task 1
        scheduler.scheduleWithFixedDelay(
            () -> jobWorker.processNextJob(),
            Duration.ofSeconds(1)
        );

        // Task 2
        scheduler.scheduleWithFixedDelay(
            () -> jobWorker.processNextJob(),
            Duration.ofSeconds(1)
        );

    }
}
