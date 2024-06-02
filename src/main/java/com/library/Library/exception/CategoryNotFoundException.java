package com.library.Library.exception;

public class CategoryNotFoundException extends Exception{
    public CategoryNotFoundException(){
        super("Category not found!");
    }

}
