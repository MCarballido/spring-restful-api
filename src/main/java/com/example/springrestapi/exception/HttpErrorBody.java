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
    private List<String> details;

    public HttpErrorBody() {
        timestamp = LocalDateTime.now();
        details = new LinkedList<>();
    }

    public HttpErrorBody(HttpStatus status) {
        this();
        this.status = status;
    }

    public HttpErrorBody(HttpStatus status, String message) {
        this();
        this.status = status;
        this.message = message;
    }

    public void addDetail(String detail) {
        this.details.add(detail);
    }

    public void removeDetail(String detail) {
        this.details.remove(detail);
    }
}