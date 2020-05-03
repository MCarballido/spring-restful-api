package com.example.springrestapi.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {
//public class GlobalControllerAdvice {

    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(
        HttpMessageNotReadableException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request
    ) {
        HttpErrorBody errorBody = new HttpErrorBody(status, ex.getLocalizedMessage());
        return buildResponseEntity(errorBody);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request
    ) {
        HttpErrorBody errorBody = new HttpErrorBody(status, ex.getLocalizedMessage());

        for (FieldError err : ex.getBindingResult().getFieldErrors()) {
            errorBody.addDetail(err.getField() + ": " + err.getDefaultMessage());
        }

        for (ObjectError err : ex.getBindingResult().getGlobalErrors()) {
            errorBody.addDetail(err.getObjectName() + ": " + err.getDefaultMessage());
        }

        return buildResponseEntity(errorBody);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleMethodArgumentMismatch(MethodArgumentTypeMismatchException ex) {
        HttpErrorBody errorBody = new HttpErrorBody(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
        errorBody.addDetail(ex.getName() + " should be of type " + ex.getRequiredType().getName());
        return buildResponseEntity(errorBody);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleException(RuntimeException ex) {
        HttpErrorBody httpErrorBody = new HttpErrorBody(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage());
        return buildResponseEntity(httpErrorBody);
    }

//    @ExceptionHandler
//    public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
//        HttpErrorBody httpErrorBody = new HttpErrorBody(HttpStatus.NOT_FOUND, ex);
//        return buildResponseEntity(httpErrorBody);
//    }
//
//    @ExceptionHandler
//    public ResponseEntity<Object> handleInvalidMethodData(MethodArgumentNotValidException ex) {
//        HttpErrorBody httpErrorBody = new HttpErrorBody(HttpStatus.BAD_REQUEST, ex);
//        ex.getBindingResult().getAllErrors().forEach(error -> {
//            httpErrorBody.addSubError(error.getDefaultMessage());
//        });
//        return buildResponseEntity(httpErrorBody);
//    }

    private ResponseEntity<Object> buildResponseEntity(HttpErrorBody httpErrorBody) {
        return new ResponseEntity<>(httpErrorBody, httpErrorBody.getStatus());
    }
}
