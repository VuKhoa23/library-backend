package com.library.Library.controller;

import com.library.Library.dto.BookDTO;
import com.library.Library.entity.Book;
import com.library.Library.exception.CategoryNotFoundException;
import com.library.Library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("")
    public ResponseEntity<String> addBook(@RequestBody BookDTO bookDTO) {
        System.out.println(bookDTO);
        if (bookDTO.getCategoryId() == null) {
            return new ResponseEntity<>("Please provide category.", HttpStatus.BAD_REQUEST);
        }
        if (bookDTO.getName() == null || bookDTO.getName().isEmpty()) {
            return new ResponseEntity<>("Please provide book's name.", HttpStatus.BAD_REQUEST);
        }
        try{
            bookService.save(bookDTO);
        } catch (CategoryNotFoundException e) {
            return new ResponseEntity<>("Category not found !", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Book created", HttpStatus.OK);
    }

    @GetMapping("")
    public @ResponseBody List<Book> getBooks(){
        return bookService.findAll();
    }
}
