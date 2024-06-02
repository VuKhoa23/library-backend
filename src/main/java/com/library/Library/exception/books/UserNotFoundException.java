package com.library.Library.exception.books;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(){
        super("User not found!");
    }
}
