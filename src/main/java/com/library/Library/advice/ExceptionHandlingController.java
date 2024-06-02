package com.library.Library.advice;

import com.library.Library.dto.ErrorResponseDTO;
import com.library.Library.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class ExceptionHandlingController {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleException(HttpMessageNotReadableException e) {
        // invalid format like: String cannot convert to Long, Integer, etc
        return new ResponseEntity<>(ErrorResponseDTO.builder().message("Invalid format (Numbers cannot be converted to Strings).").build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handle404(NoResourceFoundException e) {
        return new ResponseEntity<>(ErrorResponseDTO.builder().message("No resource found.").build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotSupported(HttpRequestMethodNotSupportedException e) {
        return new ResponseEntity<>(ErrorResponseDTO.builder().message("Method not allowed.").build(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleBookNotFound(BookNotFoundException e) {
        return new ResponseEntity<>(ErrorResponseDTO.builder().message("Book not found!").build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFound(UserNotFoundException e) {
        return new ResponseEntity<>(ErrorResponseDTO.builder().message("User not found!").build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleCategoryNotFound(CategoryNotFoundException e) {
        return new ResponseEntity<>(ErrorResponseDTO.builder().message("Category not found.").build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoMoreBookException.class)
    public ResponseEntity<ErrorResponseDTO> handleCategoryNotFound(NoMoreBookException e) {
        return new ResponseEntity<>(ErrorResponseDTO.builder().message("Not enough book!").build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidRequestParameterException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidParameter(InvalidRequestParameterException e) {
        return new ResponseEntity<>(ErrorResponseDTO.builder().message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
    }
}

