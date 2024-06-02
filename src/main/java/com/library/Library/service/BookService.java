package com.library.Library.service;

import com.library.Library.dto.BookDTO;
import com.library.Library.entity.Book;
import com.library.Library.entity.Category;
import com.library.Library.exception.BookNotFoundException;
import com.library.Library.exception.CategoryNotFoundException;
import com.library.Library.exception.InvalidRequestParameterException;
import com.library.Library.exception.UserNotFoundException;
import com.library.Library.repository.BookRepository;
import com.library.Library.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookService {
    private BookRepository bookRepository;
    private CategoryRepository categoryRepository;
    @Autowired
    public BookService(BookRepository bookRepository, CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }
    public Book save(BookDTO bookDto) throws CategoryNotFoundException, InvalidRequestParameterException {
        if (bookDto.getName() == null || bookDto.getName().isEmpty()) {
            throw new InvalidRequestParameterException("Book's name cannot be empty");
        }
        if (bookDto.getCategoryId() == null){
            throw new InvalidRequestParameterException("Book's category cannot be empty");
        }
        Category category = categoryRepository.findById(bookDto.getCategoryId()).orElseThrow(CategoryNotFoundException::new);
        Book book = new Book(bookDto.getName(), category);
        return bookRepository.save(book);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(Long id) throws BookNotFoundException {
        return bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    }

    public Book editBook(Long id, BookDTO bookDTO) throws BookNotFoundException, InvalidRequestParameterException, CategoryNotFoundException {
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        if (bookDTO.getName() == null || bookDTO.getName().isEmpty()) {
            throw new InvalidRequestParameterException("Book's name cannot be empty");
        }
        if (bookDTO.getCategoryId() == null){
            throw new InvalidRequestParameterException("Book's category cannot be empty");
        }
        Category category = categoryRepository.findById(bookDTO.getCategoryId()).orElseThrow(CategoryNotFoundException::new);
        book.setCategory(category);
        book.setName(bookDTO.getName());
        bookRepository.save(book);
        return book;
    }

    public Book addQuantity(Long id, Long quantity) throws InvalidRequestParameterException, BookNotFoundException {
        if (quantity == null){
            throw new InvalidRequestParameterException("Please provide the quantity you want to add");
        }
        if (quantity <= 0L){
            throw new InvalidRequestParameterException("Quantity must be greater than 0");
        }
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        book.setQuantity(book.getQuantity() + quantity);
        bookRepository.save(book);
        return book;
    }
}
