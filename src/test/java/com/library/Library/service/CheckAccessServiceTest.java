package com.library.Library.service;

import com.library.Library.entity.LibraryUser;
import com.library.Library.entity.Role;
import com.library.Library.exception.UserNotFoundException;
import com.library.Library.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckAccessServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private CheckAccessService checkAccessService;

    @Test
    public void ThrowUserNotFoundException(){
        String currentUsername = "username";

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(currentUsername);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByUsername(currentUsername)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () ->
                checkAccessService.getCurrentUser());
        assertEquals("User not found!", exception.getMessage());
    }

    @Test
    public void GetCurrentUserSuccessfully() throws UserNotFoundException {
        String currentUsername = "username";
        LibraryUser currentUser = new LibraryUser();
        currentUser.setUsername(currentUsername);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(currentUsername);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByUsername(currentUsername)).thenReturn(Optional.of(currentUser));

        LibraryUser expectedUser = checkAccessService.getCurrentUser();

        assertEquals(expectedUser, currentUser);
    }

    @Test
    public void ReturnTrue_IsAdmin(){
        LibraryUser user = new LibraryUser();
        user.setRoles(List.of(new Role("ADMIN")));

        boolean isAdmin = checkAccessService.isAdmin(user);
        assertEquals(isAdmin, true);
    }

    @Test
    public void ReturnFalse_IsNotAdmin(){
        LibraryUser user = new LibraryUser();
        user.setRoles(List.of(new Role("USER")));

        boolean isAdmin = checkAccessService.isAdmin(user);
        assertEquals(isAdmin, false);
    }
}
