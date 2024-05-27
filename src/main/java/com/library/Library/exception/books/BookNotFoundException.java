package com.library.Library.exception.books;

public class BookNotFoundException extends Exception{
    public BookNotFoundException(){
        super("Book not found !");
    }
}
