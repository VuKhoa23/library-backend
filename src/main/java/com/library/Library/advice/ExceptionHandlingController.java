package com.library.Library.advice;

import com.library.Library.dto.ResponseDTO;
import com.library.Library.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class ExceptionHandlingController {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDTO> handleException(HttpMessageNotReadableException e) {
        // invalid format like: String cannot convert to Long, Integer, etc
        return new ResponseEntity<>(ResponseDTO.builder().message("Invalid format (Numbers cannot be converted to Strings).").build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ResponseDTO> handle404(NoResourceFoundException e) {
        return new ResponseEntity<>(ResponseDTO.builder().message("No resource found.").build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseDTO> handleNotSupported(HttpRequestMethodNotSupportedException e) {
        return new ResponseEntity<>(ResponseDTO.builder().message("Method not allowed.").build(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleBookNotFound(BookNotFoundException e) {
        return new ResponseEntity<>(ResponseDTO.builder().message("Book not found!").build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleUserNotFound(UserNotFoundException e) {
        return new ResponseEntity<>(ResponseDTO.builder().message("User not found!").build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleCategoryNotFound(CategoryNotFoundException e) {
        return new ResponseEntity<>(ResponseDTO.builder().message("Category not found.").build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoMoreBookException.class)
    public ResponseEntity<ResponseDTO> handleCategoryNotFound(NoMoreBookException e) {
        return new ResponseEntity<>(ResponseDTO.builder().message("Not enough book!").build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidRequestParameterException.class)
    public ResponseEntity<ResponseDTO> handleInvalidParameter(InvalidRequestParameterException e) {
        return new ResponseEntity<>(ResponseDTO.builder().message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GetRentedBookDeniedException.class)
    public ResponseEntity<ResponseDTO> handleGetRentedBookDenied(GetRentedBookDeniedException e) {
        return new ResponseEntity<>(ResponseDTO.builder().message(e.getMessage()).build(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseDTO> handleAdminCannotRentBook(AccessDeniedException e) {
        return new ResponseEntity<>(ResponseDTO.builder().message(e.getMessage()).build(), HttpStatus.FORBIDDEN);
    }
}

