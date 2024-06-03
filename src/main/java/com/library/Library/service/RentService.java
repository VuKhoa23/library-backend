package com.library.Library.service;

import com.library.Library.dto.RentDTO;
import com.library.Library.entity.Book;
import com.library.Library.entity.LibraryUser;
import com.library.Library.entity.Rent;
import com.library.Library.exception.BookNotFoundException;
import com.library.Library.exception.NoMoreBookException;
import com.library.Library.exception.UserNotFoundException;
import com.library.Library.repository.BookRepository;
import com.library.Library.repository.RentRepository;
import com.library.Library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Book> getRentedBooks(Long userId) throws BookNotFoundException {
        List<Rent> rents = rentRepository.findByUserId(userId);
        List<Book> books = new ArrayList<>();
        for(Rent rent : rents){
            books.add(rent.getBook());
        }
        if(books.size() == 0){
            throw new BookNotFoundException();
        }
        return books;
    }
}
