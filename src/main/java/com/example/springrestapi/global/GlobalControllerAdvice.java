package com.example.springrestapi.global;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
//public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {
public class GlobalControllerAdvice {

//    @Override
//    @NonNull
//    public ResponseEntity<Object> handleHttpMessageNotReadable(
//        @NonNull HttpMessageNotReadableException ex,
//        @NonNull HttpHeaders headers,
//        @NonNull HttpStatus status,
//        @NonNull WebRequest request
//    ) {
//        String error = "Malformed JSON request.";
//        return buildResponseEntity(new HttpErrorBody(HttpStatus.BAD_REQUEST, error, ex));
//    }

    @ExceptionHandler
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        HttpErrorBody httpErrorBody = new HttpErrorBody(HttpStatus.NOT_FOUND, ex);
        return buildResponseEntity(httpErrorBody);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        HttpErrorBody httpErrorBody = new HttpErrorBody(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        return buildResponseEntity(httpErrorBody);
    }

    private ResponseEntity<Object> buildResponseEntity(HttpErrorBody httpErrorBody) {
        return new ResponseEntity<>(httpErrorBody, httpErrorBody.getStatus());
    }
}
