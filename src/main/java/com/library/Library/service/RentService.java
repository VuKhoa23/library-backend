package com.library.Library.service;

import com.library.Library.dto.RentDTO;
import com.library.Library.entity.Book;
import com.library.Library.entity.LibraryUser;
import com.library.Library.entity.Rent;
import com.library.Library.exception.*;
import com.library.Library.repository.BookRepository;
import com.library.Library.repository.RentRepository;
import com.library.Library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class RentService{
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private RentRepository rentRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void userRentBook(RentDTO rentDTO) throws BookNotFoundException, NoMoreBookException, UserNotFoundException {
        // Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
        // Rent bookRent = rentRepository.findByBook(book).orElseThrow(BookNotFoundException::new);
        // LibraryUser user = userRepository.findByRent(bookRent).orElseThrow(UserNotFoundException::new);
        // LibraryUser user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        Book book = bookRepository.findById(rentDTO.getBookId()).orElseThrow(BookNotFoundException::new);

        if(book.getQuantity() == 0){   // no more book
            throw new NoMoreBookException();
        }

        LibraryUser user = userRepository.findById(rentDTO.getUserId()).orElseThrow(UserNotFoundException::new);

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

    public List<Rent> findByBookId(Long bookId){
        return rentRepository.findByBookId(bookId);
    }

    public List<Rent> findByUserId(Long userId){
        return rentRepository.findByBookId(userId);
    }

    public List<Book> getRentedBooks(Long userId) throws UserNotFoundException, GetRentedBookDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        LibraryUser currentUser = userRepository.findByUsername(currentUsername).orElseThrow(UserNotFoundException::new);

        System.out.println("role " + currentUser.getRoles());
        if (currentUser.getId().equals(userId) || currentUser.getRoles().stream().anyMatch(role -> "ADMIN".equals(role.getName()))) {
            List<Book> books = new ArrayList<>();
            List<Rent> rents = rentRepository.findByUserId(userId);
            for (Rent rent : rents) {
                books.add(rent.getBook());
            }
            return books;
        } else {
            throw new GetRentedBookDeniedException("You are not allowed to view books rented by this user.");
        }
    }
}
