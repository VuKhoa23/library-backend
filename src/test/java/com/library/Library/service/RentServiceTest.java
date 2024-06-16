package com.library.Library.service;

import com.library.Library.dto.RentDTO;
import com.library.Library.entity.Book;
import com.library.Library.entity.LibraryUser;
import com.library.Library.entity.Rent;
import com.library.Library.entity.Role;
import com.library.Library.exception.*;
import com.library.Library.repository.BookRepository;
import com.library.Library.repository.RentRepository;
import com.library.Library.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class RentServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private RentRepository rentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CheckAccessService checkAccessService;
    @InjectMocks
    private RentService rentService;

    // function should throw BookNotFoundException if book's id not exists
    @Test
    public void ThrowBookNotFoundException(){
        RentDTO rentDTO = new RentDTO();
        rentDTO.setUserId(1L);
        rentDTO.setBookId(1L);
        rentDTO.setEndDate(new Date());

        when(bookRepository.findById(rentDTO.getBookId())).thenReturn(Optional.empty());
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

        when(bookRepository.findById(rentDTO.getBookId())).thenReturn(Optional.ofNullable(book));
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

        when(bookRepository.findById(rentDTO.getBookId())).thenReturn(Optional.ofNullable(book));
        when(userRepository.findById(rentDTO.getUserId())).thenReturn(Optional.empty());

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

        when(bookRepository.findById(rentDTO.getBookId())).thenReturn(Optional.of(book));
        when(userRepository.findById(rentDTO.getUserId())).thenReturn(Optional.of(user));

        rentService.userRentBook(rentDTO);

        verify(rentRepository, times(1)).save(any(Rent.class));
        verify(bookRepository, times(1)).save(any(Book.class));
        assertEquals(4L, book.getQuantity().longValue());
    }

    @Test
    public void ThrowRentBookDenied() throws UserNotFoundException {
        Long requestedUserId = 1L;
        Long currentUserId = 2L;

        LibraryUser currentUser = new LibraryUser();
        currentUser.setId(currentUserId);
        currentUser.setRoles(List.of(new Role("USER")));

        when(checkAccessService.getCurrentUser()).thenReturn(currentUser);
        when(checkAccessService.isAdmin(currentUser)).thenReturn(false);

        GetRentedBookDeniedException exception = assertThrows(GetRentedBookDeniedException.class, () ->
                rentService.getRentedBooks(requestedUserId));
        assertEquals("You are not allowed to view books rented by this user!", exception.getMessage());
    }

    @Test
    public void AdminCanViewAnyUserBooks_GetRentedBooks() throws UserNotFoundException, GetRentedBookDeniedException {
        // Arrange
        Long userId = 1L;
        Long adminId = 2L;

        LibraryUser admin = new LibraryUser();
        admin.setId(adminId);
        admin.setRoles(List.of(new Role("ADMIN")));

        List<Rent> rents = new ArrayList<>();

        Rent rent1 = new Rent();
        Book book1 = Book.builder().build();
        rent1.setBook(book1);

        Rent rent2 = new Rent();
        Book book2 = Book.builder().build();
        rent2.setBook(book2);

        rents.add(rent1);
        rents.add(rent2);

        when(checkAccessService.getCurrentUser()).thenReturn(admin);
        when(checkAccessService.isAdmin(admin)).thenReturn(true);
        when(rentRepository.findByUserId(userId)).thenReturn(rents);

        // Act
        List<Book> books = rentService.getRentedBooks(userId);

        // Assert
        assertEquals(2, books.size());
        assertTrue(books.contains(book1));
        assertTrue(books.contains(book2));
    }

    @Test
    public void UserCanOnlyViewTheirBooks_GetRentedBooks() throws UserNotFoundException, GetRentedBookDeniedException {
        // Arrange
        Long userId = 1L;

        LibraryUser user = new LibraryUser();
        user.setId(userId);
        user.setRoles(List.of(new Role("USER")));

        List<Rent> rents = new ArrayList<>();

        Rent rent1 = new Rent();
        Book book1 = Book.builder().build();
        rent1.setBook(book1);

        Rent rent2 = new Rent();
        Book book2 = Book.builder().build();
        rent2.setBook(book2);

        rents.add(rent1);
        rents.add(rent2);

        when(checkAccessService.getCurrentUser()).thenReturn(user);
        when(rentRepository.findByUserId(userId)).thenReturn(rents);

        // Act
        List<Book> books = rentService.getRentedBooks(userId);

        // Assert
        assertEquals(2, books.size());
        assertTrue(books.contains(book1));
        assertTrue(books.contains(book2));
    }

    @Test
    public void failedCase(){
        assertEquals(1, 2);
    }
}
