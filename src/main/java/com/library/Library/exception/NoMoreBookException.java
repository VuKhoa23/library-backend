package com.library.Library.exception;

public class NoMoreBookException extends Exception{
    public NoMoreBookException(){
        super("No more book in stock");
    }
}
