package com.example.springrestapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

    @ExceptionHandler
    public ResponseEntity<Object> handleInvalidMethodData(MethodArgumentNotValidException ex) {
        HttpErrorBody httpErrorBody = new HttpErrorBody(HttpStatus.BAD_REQUEST, ex);
        ex.getBindingResult().getAllErrors().forEach(error -> {
            httpErrorBody.addSubError(error.getDefaultMessage());
        });
        return buildResponseEntity(httpErrorBody);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleException(RuntimeException ex) {
        HttpErrorBody httpErrorBody = new HttpErrorBody(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        return buildResponseEntity(httpErrorBody);
    }

    private ResponseEntity<Object> buildResponseEntity(HttpErrorBody httpErrorBody) {
        return new ResponseEntity<>(httpErrorBody, httpErrorBody.getStatus());
    }
}
