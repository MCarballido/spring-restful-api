package com.example.springrestapi.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Data
public class HttpErrorBody {

    private HttpStatus status;
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;
    private List<String> details;

    public HttpErrorBody() {
        timestamp = LocalDateTime.now();
        details = new LinkedList<>();
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

    public void addSubError(String subError) {
        this.details.add(subError);
    }

    public void removeSubError(String subError) {
        this.details.remove(subError);
    }
}