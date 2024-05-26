package com.library.Library.repository;

import com.library.Library.entity.LibraryUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<LibraryUser, Long> {
    Optional<LibraryUser> findByUsername(String username);
    Boolean existsByUsername(String username);
}
