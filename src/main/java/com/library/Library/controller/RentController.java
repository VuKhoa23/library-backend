package com.library.Library.controller;

import com.library.Library.dto.RentDTO;
import com.library.Library.exception.books.BookNotFoundException;
import com.library.Library.exception.books.NoMoreBookException;
import com.library.Library.exception.books.UserNotFoundException;
import com.library.Library.service.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rent")
public class RentController {
    private RentService rentService;

    @Autowired
    public RentController(RentService rentService){
        this.rentService = rentService;
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<String> rentBook(@PathVariable Long bookId, @RequestBody RentDTO rentDTO) {
        try{
            rentService.userRentBook(bookId, rentDTO);
            return new ResponseEntity("Book rented successfully!", HttpStatus.OK);
        }
        catch(UserNotFoundException | NoMoreBookException | BookNotFoundException e){
            return new ResponseEntity("Book rented failed!", HttpStatus.BAD_REQUEST);
        }
    }
}
