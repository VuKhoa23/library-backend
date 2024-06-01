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
@RequestMapping("api/rent")
public class RentController {
    private RentService rentService;

    @Autowired
    public RentController(RentService rentService) {
        this.rentService = rentService;
    }

    @PostMapping("")
    public ResponseEntity<String> rentBook(@RequestBody RentDTO rentDTO) {
        try {
            rentService.userRentBook(rentDTO);
            return new ResponseEntity("Book rented successfully!", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity("User not found!", HttpStatus.BAD_REQUEST);
        } catch (NoMoreBookException e) {
            return new ResponseEntity("Not enough book!", HttpStatus.BAD_REQUEST);
        } catch (BookNotFoundException e) {
            return new ResponseEntity("Book not found!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/test")
    public ResponseEntity<String> test() {

        return new ResponseEntity("Book rented successfully!", HttpStatus.OK);
    }
}
