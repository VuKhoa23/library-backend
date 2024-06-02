package com.library.Library.controller;

import com.library.Library.dto.BookDTO;
import com.library.Library.entity.Book;
import com.library.Library.exception.BookNotFoundException;
import com.library.Library.exception.CategoryNotFoundException;
import com.library.Library.exception.InvalidRequestParameterException;
import com.library.Library.exception.UserNotFoundException;
import com.library.Library.service.BookService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("")
    public @ResponseBody Book addBook(@RequestBody BookDTO bookDTO) throws CategoryNotFoundException, InvalidRequestParameterException {
        try {
            return bookService.save(bookDTO);
        } catch (CategoryNotFoundException e) {
            throw new CategoryNotFoundException();
        } catch (InvalidRequestParameterException e) {
            throw new InvalidRequestParameterException(e.getMessage());
        }
    }

    @GetMapping("")
    public @ResponseBody List<Book> getBooks() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public @ResponseBody Book getBook(@PathVariable("id") Long id) throws BookNotFoundException {
        try {
            return bookService.findById(id);
        } catch (BookNotFoundException e) {
            throw new BookNotFoundException();
        }
    }

    @PutMapping("/{id}")
    public @ResponseBody Book editBook(@PathVariable("id") Long id, @RequestBody BookDTO bookDTO) throws BookNotFoundException, InvalidRequestParameterException, CategoryNotFoundException {
        try {
            return bookService.editBook(id, bookDTO);
        } catch (BookNotFoundException e) {
            throw new BookNotFoundException();
        } catch (InvalidRequestParameterException e) {
            throw new InvalidRequestParameterException(e.getMessage());
        } catch (CategoryNotFoundException e) {
            throw new CategoryNotFoundException();
        }
    }

    @PatchMapping("/{id}")
    public @ResponseBody Book addQuantity(@PathVariable("id") Long id, @RequestBody Map<String, Long> payload) throws InvalidRequestParameterException, BookNotFoundException {
        try {
            return bookService.addQuantity(id, payload.get("quantity"));
        } catch (InvalidRequestParameterException e) {
            throw new InvalidRequestParameterException(e.getMessage());
        } catch (BookNotFoundException e) {
            throw new BookNotFoundException();
        }
    }
}
