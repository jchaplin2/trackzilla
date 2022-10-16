package com.trackzilla.controller;

import com.trackzilla.exception.ErrorMessage;
import com.trackzilla.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  ResponseEntity exceptionHandler(ResourceNotFoundException e, WebRequest request) {
    ErrorMessage message = new ErrorMessage(
            HttpStatus.NOT_FOUND.value(),
            new Date(),
            e.getMessage(),
            request.getDescription(false)
    );

    return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(NoSuchElementException.class)
  ResponseEntity elementExceptionHandler(ResourceNotFoundException e, WebRequest request) {
    ErrorMessage message = new ErrorMessage(
            HttpStatus.NOT_FOUND.value(),
            new Date(),
            e.getMessage(),
            request.getDescription(false)
    );

    return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  ResponseEntity globalExceptionHandler(Exception e, WebRequest request) {
    ErrorMessage message = new ErrorMessage(
            HttpStatus.NOT_FOUND.value(),
            new Date(),
            e.getMessage(),
            request.getDescription(false)
    );

    return new ResponseEntity(message, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
