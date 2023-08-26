package com.playfulCloud.cruddemo.customer.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorDetail {
    private final LocalDateTime timestamp;
    private final String message;
    private final String details;



}
