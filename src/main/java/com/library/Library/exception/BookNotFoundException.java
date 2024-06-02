package com.library.Library.exception;

public class BookNotFoundException extends Exception{
    public BookNotFoundException(){
        super("Book not found!");
    }
}
