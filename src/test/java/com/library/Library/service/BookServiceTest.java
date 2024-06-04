package com.library.Library.service;

import com.library.Library.dto.BookDTO;
import com.library.Library.entity.Book;
import com.library.Library.entity.Category;
import com.library.Library.exception.BookNotFoundException;
import com.library.Library.exception.CategoryNotFoundException;
import com.library.Library.exception.InvalidRequestParameterException;
import com.library.Library.repository.BookRepository;
import com.library.Library.repository.CategoryRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private BookService bookService;

    // save

    @Test
    public void ThrowCateGoryNotFoundException(){
        BookDTO bookDTO = BookDTO.builder()
                .name("Book Name")
                .categoryId(1L)
                .build();
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        CategoryNotFoundException exception = Assert.assertThrows(CategoryNotFoundException.class, () ->
                bookService.save(bookDTO));

        assertEquals("Category not found!", exception.getMessage());
    }

    @Test
    public void ThrowInvalidRequest_EmptyName_SaveBook(){
        BookDTO bookDTO = BookDTO.builder()
                .name("")
                .categoryId(1L)
                .build();

        InvalidRequestParameterException exception = Assert.assertThrows(InvalidRequestParameterException.class, () ->
                bookService.save(bookDTO));

        assertEquals("Book's name cannot be empty", exception.getMessage());
    }

    @Test
    public void ThrowInvalidRequest_EmptyCategory_SaveBook(){
        BookDTO bookDTO = BookDTO.builder()
                .name("Book Name")
                .categoryId(null)
                .build();

        InvalidRequestParameterException exception = Assert.assertThrows(InvalidRequestParameterException.class, () ->
                bookService.save(bookDTO));

        assertEquals("Book's category cannot be empty", exception.getMessage());
    }

    @Test
    public void SaveBookSuccess() throws InvalidRequestParameterException, CategoryNotFoundException {
        Category category = new Category("Category Name");
        BookDTO bookDTO = BookDTO.builder().name("Book Name").categoryId(1L).build();
        Book book = Book.builder().name(bookDTO.getName()).category(category).build();

        // assume that category.id = 1L
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // assume that when book is saved, it will be saved as Book.builder().name(bookDTO.getName()).category(category).build()
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book savedBook = bookService.save(bookDTO);

        assertEquals("Book Name", savedBook.getName());
        assertEquals(category, savedBook.getCategory());
        verify(categoryRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    // findAll

    // findById

    // editBook

    // addQuantity

    @Test
    public void ThrowInvalidRequest_EmptyQuantity_AddQuantity(){
        Long bookId = 1L;
        Long quantity = null;

        InvalidRequestParameterException exception = Assert.assertThrows(InvalidRequestParameterException.class, () ->
                bookService.addQuantity(bookId, quantity));

        assertEquals("Please provide the quantity you want to add", exception.getMessage());
    }

    @Test
    public void ThrowInvalidRequest_NonPositiveQuantity_AddQuantity(){
        Long bookId = 1L;
        Long quantity = -1L;

        InvalidRequestParameterException exception = Assert.assertThrows(InvalidRequestParameterException.class, () ->
                bookService.addQuantity(bookId, quantity));

        assertEquals("Quantity must be greater than 0", exception.getMessage());
    }

    @Test
    public void AddQuantitySuccess() throws InvalidRequestParameterException, BookNotFoundException {
        // Arrange
        Long bookId = 1L;
        Long quantity = 2L;

        Book book = Book.builder()
                .id(bookId)
                .name("Book Name")
                .category(new Category("Category Name"))
                .quantity(5L)
                .build();

        Book updatedBook = Book.builder()
                .id(2L)
                .name(book.getName())
                .category(book.getCategory())
                .quantity(book.getQuantity() + quantity)
                .build();

        // Act
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        Book result = bookService.addQuantity(bookId, quantity);

        // Assert
        assertEquals(updatedBook.getQuantity(), result.getQuantity());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(any(Book.class));
    }
}
