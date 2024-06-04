package com.library.Library.controller;

import com.library.Library.dto.RentDTO;
import com.library.Library.dto.ResponseDTO;
import com.library.Library.entity.Book;
import com.library.Library.exception.BookNotFoundException;
import com.library.Library.exception.NoMoreBookException;
import com.library.Library.exception.UserNotFoundException;
import com.library.Library.service.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/rent")
public class RentController {
    private RentService rentService;

    @Autowired
    public RentController(RentService rentService) {
        this.rentService = rentService;
    }

    @PostMapping("")
    public ResponseEntity<ResponseDTO> rentBook(@RequestBody RentDTO rentDTO) throws UserNotFoundException, NoMoreBookException, BookNotFoundException {
        try {
            rentService.userRentBook(rentDTO);
            return new ResponseEntity<>(ResponseDTO.builder().message("Book rented successfully!").build(), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        } catch (NoMoreBookException e) {
            throw new NoMoreBookException();
        } catch (BookNotFoundException e) {
            throw new BookNotFoundException();
        }
    }

    @GetMapping("{userId}")
    public @ResponseBody List<Book> getRentedBooks(@PathVariable("userId") Long userId) throws BookNotFoundException {
        try{
            List<Book> books = rentService.getRentedBooks(userId);
            return books;
        }
        catch (BookNotFoundException e) {
            throw new BookNotFoundException();
        }
    }

    @PostMapping("/test")
    public ResponseEntity<String> test() {

        return new ResponseEntity("Book rented successfully!", HttpStatus.OK);
    }
}
