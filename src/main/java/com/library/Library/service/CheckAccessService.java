package com.library.Library.service;

import com.library.Library.entity.LibraryUser;
import com.library.Library.exception.UserNotFoundException;
import com.library.Library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CheckAccessService {
    private UserRepository userRepository;

    @Autowired
    public CheckAccessService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LibraryUser getCurrentUser() throws UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return userRepository.findByUsername(currentUsername).orElseThrow(UserNotFoundException::new);
    }

    public boolean isAdmin(LibraryUser user) {
        return user.getRoles().stream().anyMatch(role -> "ADMIN".equals(role.getName()));
    }
}

