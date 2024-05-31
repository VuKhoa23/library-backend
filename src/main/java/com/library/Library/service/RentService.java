package com.library.Library.service;

import com.library.Library.dto.RentDTO;
import com.library.Library.entity.Book;
import com.library.Library.entity.LibraryUser;
import com.library.Library.entity.Rent;
import com.library.Library.exception.books.BookNotFoundException;
import com.library.Library.exception.books.NoMoreBookException;
import com.library.Library.exception.books.UserNotFoundException;
import com.library.Library.repository.BookRepository;
import com.library.Library.repository.RentRepository;
import com.library.Library.repository.UserRepository;
import jakarta.transaction.Transactional;
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

    @Transactional
    public void userRentBook(Long bookId, RentDTO rentDTO) throws BookNotFoundException, NoMoreBookException, UserNotFoundException {
        // Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
        // Rent bookRent = rentRepository.findByBook(book).orElseThrow(BookNotFoundException::new);
        // LibraryUser user = userRepository.findByRent(bookRent).orElseThrow(UserNotFoundException::new);
        // LibraryUser user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);

        if(book.getQuantity() == 0){   // no more book
            throw new NoMoreBookException();
        }

        String username = rentDTO.getUsername();
        LibraryUser user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

        Rent rent = new Rent();
        rent.setBook(book);
        rent.setUser(user);
        rent.setStartDate(new Date());
        rent.setEndDate(rentDTO.getEndDate());
        rent.setStatus(false);

        book.setQuantity(book.getQuantity() - 1);

        rentRepository.save(rent);
        bookRepository.save(book);
    }
}
