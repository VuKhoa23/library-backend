package com.library.Library.exception;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(){
        super("User not found!");
    }
}
