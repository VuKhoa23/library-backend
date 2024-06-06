package com.library.Library.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.library.Library.dto.RentDTO;
import com.library.Library.entity.Book;
import com.library.Library.exception.BookNotFoundException;
import com.library.Library.exception.GetRentedBookDeniedException;
import com.library.Library.exception.NoMoreBookException;
import com.library.Library.exception.UserNotFoundException;
import com.library.Library.service.RentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = RentController.class)
public class RentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RentService rentService;

    @Autowired
    private ObjectMapper objectMapper;

    private RentDTO rentDTO;

    @BeforeEach
    public void setUp(){
        rentDTO = new RentDTO();
        rentDTO.setUserId(100L);
        rentDTO.setBookId(1L);
        rentDTO.setEndDate(new Date());
    }

    @Test
    public void rentBookNotFound() throws Exception {
        doThrow(new BookNotFoundException()).when(rentService).userRentBook(rentDTO);

        ResultActions resultActions = mockMvc.perform(post("/api/rent")
                .with(csrf())
                .with(user("user").roles("ADMIN", "USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rentDTO)));

        resultActions.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found!"));
    }

    @Test
    public void rentNotEnoughBook() throws Exception {
        doThrow(new NoMoreBookException()).when(rentService).userRentBook(rentDTO);

        ResultActions resultActions = mockMvc.perform(post("/api/rent")
                .with(csrf())
                .with(user("user").roles("ADMIN", "USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rentDTO)));

        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Not enough book!"));
    }

    @Test
    public void rentUserNotFound() throws Exception {
        doThrow(new UserNotFoundException()).when(rentService).userRentBook(rentDTO);

        ResultActions resultActions = mockMvc.perform(post("/api/rent")
                .with(csrf())
                .with(user("user").roles("ADMIN", "USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rentDTO)));

        resultActions.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found!"));
    }

    @Test
    public void rentBookSuccessfully() throws Exception {
        Mockito.doNothing().when(rentService).userRentBook(rentDTO);    // assume rent successfully

        ResultActions result = mockMvc.perform(post("/api/rent")
                .with(csrf())
                .with(user("user").roles("ADMIN", "USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rentDTO)));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Book rented successfully!"));
    }

    @Test
    @WithMockUser(authorities = {"USER", "ADMIN"})
    public void GetRentedBooksSuccess() throws Exception {
        // Arrange
        Long userId = 1L;
        List<Book> books = new ArrayList<>();
        books.add(Book.builder().build());
        books.add(Book.builder().build());

        when(rentService.getRentedBooks(userId)).thenReturn(books);

        // Act
        ResultActions resultActions = mockMvc.perform(get("/api/rent/{userId}", userId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void GetRentedBookDenied_Return403() throws Exception {
        // Arrange
        Long userId = 1L;
        doThrow(GetRentedBookDeniedException.class).when(rentService).getRentedBooks(userId);

        // Act
        ResultActions resultActions = mockMvc.perform(get("/api/rent/{userId}", userId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void GetRentedBook_UserNotFound_Return404() throws Exception {
        // Arrange
        Long userId = 1L;
        doThrow(UserNotFoundException.class).when(rentService).getRentedBooks(userId);

        // Act
        ResultActions resultActions = mockMvc.perform(get("/api/rent/{userId}", userId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(status().isNotFound());
    }
}

