package com.library.Library.service.helper;

import com.library.Library.entity.LibraryUser;
import com.library.Library.exception.UserNotFoundException;
import com.library.Library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CheckAccessService {
    @Autowired
    private UserRepository userRepository;

    public boolean isAccess() throws UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        LibraryUser currentUser = userRepository.findByUsername(currentUsername).orElseThrow(UserNotFoundException::new);

        return true;
    }
}
