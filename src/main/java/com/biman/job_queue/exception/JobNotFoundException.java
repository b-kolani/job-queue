package com.biman.job_queue.exception;

public class JobNotFoundException extends RuntimeException{
    
    public JobNotFoundException(String message) {
        super(message);
    }
}
