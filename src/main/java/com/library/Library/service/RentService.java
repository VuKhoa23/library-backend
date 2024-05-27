package com.library.Library.service;

import com.library.Library.entity.Book;
import com.library.Library.exception.books.BookNotFoundException;
import com.library.Library.exception.books.NoMoreBookException;
import com.library.Library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RentService{
    @Autowired
    private BookRepository bookRepository;
    public void userRentBook(Long bookId, Long userId) throws BookNotFoundException, NoMoreBookException {
        Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
        if (book.getQuantity() == 0){
            throw new NoMoreBookException();
        }
    }
}
