package com.example.springrestapi.global;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class HttpErrorBody {

    private HttpStatus status;
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;
//    private List<> subErrors;

    public HttpErrorBody() {
        timestamp = LocalDateTime.now();
    }

    public HttpErrorBody(HttpStatus status) {
        this();
        this.status = status;
    }

    public HttpErrorBody(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }

    public HttpErrorBody(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }
}