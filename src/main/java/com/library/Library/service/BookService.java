package com.library.Library.service;

import com.library.Library.dto.BookDTO;
import com.library.Library.entity.Book;
import com.library.Library.entity.Category;
import com.library.Library.exception.CategoryNotFoundException;
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
    public Book save(BookDTO bookDto) throws CategoryNotFoundException {
        Category category = categoryRepository.findById(bookDto.getCategoryId()).orElseThrow(CategoryNotFoundException::new);
        Book book = new Book(bookDto.getName(), category);
        return bookRepository.save(book);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}
