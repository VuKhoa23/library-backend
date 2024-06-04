package com.library.Library.controller;

import com.library.Library.dto.RentDTO;
import com.library.Library.dto.ResponseDTO;
import com.library.Library.entity.Book;
import com.library.Library.exception.*;
import com.library.Library.service.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
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
    @PreAuthorize("hasAuthority('USER')")
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
        } catch (AccessDeniedException e) {
            throw new AccessDeniedException(e.getMessage());
        }
    }

    @GetMapping("{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public @ResponseBody List<Book> getRentedBooks(@PathVariable("userId") Long userId) throws UserNotFoundException, GetRentedBookDeniedException {
        try{
            List<Book> books = rentService.getRentedBooks(userId);
            return books;
        }
        catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        }
    }

    @PostMapping("/test")
    public ResponseEntity<String> test() {

        return new ResponseEntity("Book rented successfully!", HttpStatus.OK);
    }
}
