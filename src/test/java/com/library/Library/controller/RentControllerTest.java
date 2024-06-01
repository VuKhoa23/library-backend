package com.library.Library.controller;

import com.library.Library.dto.RentDTO;
import com.library.Library.exception.books.BookNotFoundException;
import com.library.Library.exception.books.NoMoreBookException;
import com.library.Library.exception.books.UserNotFoundException;
import com.library.Library.service.RentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RentController.class)
@WithMockUser(username = "khoa",  roles= {"USER", "LIBRARIAN"})
public class RentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private RentService rentService;

    @Autowired
    private ObjectMapper objectMapper;

    private RentDTO rentDTO;

    @BeforeEach
    public void setUp(){
        rentDTO = new RentDTO();
        rentDTO.setUserId(1L);
        rentDTO.setBookId(1L);
        rentDTO.setEndDate(new Date());
    }

    @Test
    public void rentBookNotFound() throws Exception {
        Mockito.doThrow(new BookNotFoundException()).when(rentService).userRentBook(rentDTO);

        ResultActions resultActions = mockMvc.perform(post("/api/rent")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rentDTO)));

        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().string("Book rented failed!"));
    }

    @Test
    public void rentNotEnoughBook() throws Exception {
        Mockito.doThrow(new NoMoreBookException()).when(rentService).userRentBook(rentDTO);

        ResultActions resultActions = mockMvc.perform(post("/api/rent")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rentDTO)));

        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().string("Book rented failed!"));
    }

    @Test
    public void rentUserNotFound() throws Exception {
        Mockito.doThrow(new UserNotFoundException()).when(rentService).userRentBook(rentDTO);

        ResultActions resultActions = mockMvc.perform(post("/api/rent")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rentDTO)));

        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().string("Book rented failed!"));
    }

    @Test
    public void rentBookSuccessfully() throws Exception {
        Mockito.doNothing().when(rentService).userRentBook(rentDTO);    // assume rent successfully

        ResultActions result = mockMvc.perform(post("/api/rent")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rentDTO)));

        result.andExpect(status().isOk())
                .andExpect(content().string("Book rented successfully!"));
    }
}

