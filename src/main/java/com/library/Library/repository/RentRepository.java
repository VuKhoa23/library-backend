package com.library.Library.repository;

import com.library.Library.entity.Book;
import com.library.Library.entity.Rent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RentRepository extends JpaRepository<Rent, Long> {
    List<Rent> findByBookId(Long bookId);
    List<Rent> findByUserId(Long userId);
}
