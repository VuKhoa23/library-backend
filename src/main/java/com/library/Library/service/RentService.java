package com.library.Library.service;

import com.library.Library.entity.Book;
import com.library.Library.entity.LibraryUser;
import com.library.Library.entity.Rent;
import com.library.Library.exception.books.BookNotFoundException;
import com.library.Library.exception.books.NoMoreBookException;
import com.library.Library.exception.books.UserNotFoundException;
import com.library.Library.repository.BookRepository;
import com.library.Library.repository.RentRepository;
import com.library.Library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RentService{
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private RentRepository rentRepository;
    @Autowired
    private UserRepository userRepository;
    public void userRentBook(Long bookId, Long userId) throws BookNotFoundException, NoMoreBookException, UserNotFoundException {
        Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
        Rent bookRent = rentRepository.findByBook(book).orElseThrow(BookNotFoundException::new);
        LibraryUser user = userRepository.findByRent(bookRent).orElseThrow(UserNotFoundException::new);
        if (book.getQuantity() == 0){
            throw new NoMoreBookException();
        }
        if(bookRent != null && !bookRent.isStatus()){   // book is already borrowed
            throw new NoMoreBookException();
        }
        Rent rent = new Rent();
        rent.setBook(book);
        rent.setUser(user);
        rent.setStart_date(new Date());
    }
}
