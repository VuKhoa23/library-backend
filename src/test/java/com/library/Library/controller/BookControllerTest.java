package com.library.Library.controller;

import com.library.Library.dto.BookDTO;
import com.library.Library.entity.Book;
import com.library.Library.entity.Category;
import com.library.Library.exception.BookNotFoundException;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        Book book = new Book("Harry Potter", new Category("Fiction"));
        BookDTO dummyDTO = BookDTO.builder().build();
        Mockito.doReturn(book).when(bookService).save(dummyDTO);

        ResultActions resultActions = mockMvc.perform(post("/api/books")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dummyDTO)));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Harry Potter"))
                .andExpect(jsonPath("$.category.name").value("Fiction"));
    }

    @Test
    public void Test_GetSingleBook_Return404() throws Exception {
        Mockito.doThrow(new BookNotFoundException()).when(bookService).findById(1L);

        ResultActions resultActions = mockMvc.perform(get("/api/books/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void Test_GetSingleBook_ReturnInvalidParam_IdString() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/books/Invalid")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void Test_EditBook_ReturnBadRequest() throws Exception {
        BookDTO bookDTO = BookDTO.builder().name("").build();
        Mockito.doThrow(new InvalidRequestParameterException("Invalid parameter")).when(bookService).editBook(1L, bookDTO);

        ResultActions resultActions = mockMvc.perform(put("/api/books/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDTO)));
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void Test_EditBook_Successfully() throws Exception {
        BookDTO bookDTO = BookDTO.builder().name("Harry Potter").categoryId(1L).build();

        Mockito.doReturn(new Book("Harry Potter", new Category("Fiction"))).when(bookService).editBook(1L, bookDTO);

        ResultActions resultActions = mockMvc.perform(put("/api/books/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDTO)));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Harry Potter"))
                .andExpect(jsonPath("$.category.name").value("Fiction"));
        ;
    }

    @Test
    public void Test_AddQuantity_InvalidQuantity() throws Exception {
        Mockito.doThrow(new InvalidRequestParameterException("Not valid parameter")).when(bookService).addQuantity(1L, 0L);
        ResultActions resultActions = mockMvc.perform(patch("/api/books/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"quantity\": 0}"));

        resultActions.
                andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.message").value("Not valid parameter"))
        ;

    }

    @Test
    public void Test_AddQuantity_Return404() throws Exception {
        Mockito.doThrow(new BookNotFoundException()).when(bookService).addQuantity(1L, 1L);
        ResultActions resultActions = mockMvc.perform(patch("/api/books/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"quantity\": 1}"));

        resultActions.
                andExpect(status().isNotFound());

    }
}
