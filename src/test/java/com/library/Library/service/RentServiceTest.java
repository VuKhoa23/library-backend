package com.library.Library.service;

import com.library.Library.dto.RentDTO;
import com.library.Library.entity.Book;
import com.library.Library.exception.books.BookNotFoundException;
import com.library.Library.exception.books.NoMoreBookException;
import com.library.Library.repository.BookRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;


@RunWith(MockitoJUnitRunner.class)
public class RentServiceTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private RentService rentService;

    // function should throw BookNotFoundException if book's id not exists
    @Test
    public void ThrowBookNotFoundException(){
        Long bookId = 1L;

        RentDTO rentDTO = new RentDTO();
        rentDTO.setUsername("User");
        rentDTO.setEndDate(new Date());

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () ->
                rentService.userRentBook(bookId, rentDTO));

        assertEquals("Book not found !", exception.getMessage());
    }

    @Test
    public void ThrowNotEnoughBookException(){
        Long bookId = 1L;
        RentDTO rentDTO = new RentDTO();
        rentDTO.setUsername("User");
        rentDTO.setEndDate(new Date());

        Book book = Book.builder().name("Harry Potter").category(null).id(bookId).quantity(0L).build();

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.ofNullable(book));
        NoMoreBookException exception = assertThrows(NoMoreBookException.class, () ->
                rentService.userRentBook(bookId, rentDTO));

        assertEquals("No more book in stock", exception.getMessage());
    }
}
