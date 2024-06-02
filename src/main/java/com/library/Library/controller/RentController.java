package com.library.Library.controller;

import com.library.Library.dto.RentDTO;
import com.library.Library.exception.BookNotFoundException;
import com.library.Library.exception.NoMoreBookException;
import com.library.Library.exception.UserNotFoundException;
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
    public ResponseEntity<String> rentBook(@RequestBody RentDTO rentDTO) throws UserNotFoundException, NoMoreBookException, BookNotFoundException {
        try {
            rentService.userRentBook(rentDTO);
            return new ResponseEntity("Book rented successfully!", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        } catch (NoMoreBookException e) {
            throw new NoMoreBookException();
        } catch (BookNotFoundException e) {
            throw new BookNotFoundException();
        }
    }

    @PostMapping("/test")
    public ResponseEntity<String> test() {

        return new ResponseEntity("Book rented successfully!", HttpStatus.OK);
    }
}
