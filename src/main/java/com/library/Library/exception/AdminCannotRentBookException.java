package com.library.Library.exception;

import org.springframework.security.access.AccessDeniedException;

public class AdminCannotRentBookException extends AccessDeniedException {
    public AdminCannotRentBookException(String s){
        super(s);
    }
}
