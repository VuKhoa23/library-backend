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
        RentDTO rentDTO = new RentDTO();
        rentDTO.setBookName("Book");
        rentDTO.setUsername("User");
        rentDTO.setEndDate(new Date());

        Mockito.when(bookRepository.findByName(rentDTO.getBookName())).thenReturn(Optional.empty());
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () ->
                rentService.userRentBook(rentDTO));

        assertEquals("Book not found !", exception.getMessage());
    }

    @Test
    public void ThrowNotEnoughBookException(){
        RentDTO rentDTO = new RentDTO();
        rentDTO.setBookName("Book");
        rentDTO.setUsername("User");
        rentDTO.setEndDate(new Date());

        Book book = Book.builder().name(rentDTO.getBookName()).category(null).id(1L).quantity(0L).build();

        Mockito.when(bookRepository.findByName(rentDTO.getBookName())).thenReturn(Optional.ofNullable(book));
        NoMoreBookException exception = assertThrows(NoMoreBookException.class, () ->
                rentService.userRentBook(rentDTO));

        assertEquals("No more book in stock", exception.getMessage());
    }
}
