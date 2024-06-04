package com.library.Library.controller;

import com.library.Library.dto.BookDTO;
import com.library.Library.entity.Book;
import com.library.Library.entity.Category;
import com.library.Library.exception.CategoryNotFoundException;
import com.library.Library.exception.InvalidRequestParameterException;
import com.library.Library.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import com.fasterxml.jackson.databind.ObjectMapper;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = BookController.class)
@AutoConfigureMockMvc(addFilters = false)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void Test_AddBook_ReturnCategoryNotFound() throws Exception {
        BookDTO bookDTO = BookDTO.builder().categoryId(100L).name("Harry Potter").build();
        Mockito.doThrow(new CategoryNotFoundException()).when(bookService).save(bookDTO);

        ResultActions resultActions = mockMvc.perform(post("/api/books")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDTO)));

        resultActions.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Category not found!"));
    }

    @Test
    public void Test_AddBook_ReturnInvalidParams() throws Exception {
        BookDTO bookDTO = BookDTO.builder().categoryId(100L).name("Harry Potter").build();
        Mockito.doThrow(new InvalidRequestParameterException("Something invalid!")).when(bookService).save(bookDTO);

        ResultActions resultActions = mockMvc.perform(post("/api/books")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDTO)));

        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Something invalid!"));
    }

    @Test
    public void Test_AddBook_Successfully() throws Exception {
        Book book = new Book("Harry Potter", new Category(1L, "Fiction"));
        BookDTO dummyDTO = BookDTO.builder().build();
        Mockito.doReturn(book).when(bookService).save(dummyDTO);

        ResultActions resultActions = mockMvc.perform(post("/api/books")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dummyDTO)));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Harry Potter"))
                .andExpect(jsonPath("$.category.id").value(1))
                .andExpect(jsonPath("$.category.name").value("Fiction"));
    }



}
