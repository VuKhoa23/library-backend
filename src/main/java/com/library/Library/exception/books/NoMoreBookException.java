package com.library.Library.exception.books;

public class NoMoreBookException extends Exception{
    public NoMoreBookException(){
        super("No more book in stock");
    }
}
