package com.library.Library.service;

import com.library.Library.dto.RentDTO;
import com.library.Library.entity.Book;
import com.library.Library.entity.LibraryUser;
import com.library.Library.entity.Rent;
import com.library.Library.exception.AdminCannotRentBookException;
import com.library.Library.exception.BookNotFoundException;
import com.library.Library.exception.NoMoreBookException;
import com.library.Library.exception.UserNotFoundException;
import com.library.Library.repository.BookRepository;
import com.library.Library.repository.RentRepository;
import com.library.Library.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.any;


import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;


@RunWith(MockitoJUnitRunner.class)
public class RentServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private RentRepository rentRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RentService rentService;

    // function should throw BookNotFoundException if book's id not exists
    @Test
    public void ThrowBookNotFoundException(){
        RentDTO rentDTO = new RentDTO();
        rentDTO.setUserId(1L);
        rentDTO.setBookId(1L);
        rentDTO.setEndDate(new Date());

        Mockito.when(bookRepository.findById(rentDTO.getBookId())).thenReturn(Optional.empty());
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () ->
                rentService.userRentBook(rentDTO));

        assertEquals("Book not found!", exception.getMessage());
    }

    @Test
    public void ThrowNotEnoughBookException(){
        RentDTO rentDTO = new RentDTO();
        rentDTO.setUserId(1L);
        rentDTO.setBookId(1L);
        rentDTO.setEndDate(new Date());

        Book book = new Book("Harry Potter", null);
        book.setQuantity(0L);

        Mockito.when(bookRepository.findById(rentDTO.getBookId())).thenReturn(Optional.ofNullable(book));
        NoMoreBookException exception = assertThrows(NoMoreBookException.class, () ->
                rentService.userRentBook(rentDTO));

        assertEquals("No more book in stock", exception.getMessage());
    }

    @Test
    public void ThrowUserNotFound(){
        RentDTO rentDTO = new RentDTO();
        rentDTO.setUserId(1L);
        rentDTO.setBookId(1L);
        rentDTO.setEndDate(new Date());

        Book book = new Book("Harry Potter", null);
        book.setQuantity(5L);

        Mockito.when(bookRepository.findById(rentDTO.getBookId())).thenReturn(Optional.ofNullable(book));
        Mockito.when(userRepository.findById(rentDTO.getUserId())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () ->
                rentService.userRentBook(rentDTO));
        assertEquals("User not found!", exception.getMessage());
    }

    @Test
    public void RentBookSuccessfully() throws UserNotFoundException, NoMoreBookException, BookNotFoundException, AdminCannotRentBookException {
        RentDTO rentDTO = new RentDTO();
        rentDTO.setUserId(1L);
        rentDTO.setBookId(1L);
        rentDTO.setEndDate(new Date());

        Book book = new Book("Harry Potter", null);
        book.setQuantity(5L);
        book.setId(rentDTO.getBookId());

        LibraryUser user = new LibraryUser();
        user.setUsername("John Doe");
        user.setPassword("haha123");

        Mockito.when(bookRepository.findById(rentDTO.getBookId())).thenReturn(Optional.of(book));
        Mockito.when(userRepository.findById(rentDTO.getUserId())).thenReturn(Optional.of(user));

        rentService.userRentBook(rentDTO);

        verify(rentRepository, times(1)).save(any(Rent.class));

        verify(bookRepository, times(1)).save(any(Book.class));

        assertEquals(4L, book.getQuantity().longValue());
    }
}
