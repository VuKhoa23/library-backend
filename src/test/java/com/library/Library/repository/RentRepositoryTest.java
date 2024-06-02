package com.library.Library.repository;

import com.library.Library.entity.Book;
import com.library.Library.entity.LibraryUser;
import com.library.Library.entity.Rent;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@ActiveProfiles("test")
@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@RunWith(SpringRunner.class)
public class RentRepositoryTest {
    @Autowired
    RentRepository rentRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void SaveRent_ReturnValid(){
        // Create and save a book
        Book book = Book.builder().name("Harry Potter").category(null).quantity(5L).build();
        Book savedBook = bookRepository.save(book);

        // Create and save a user
        LibraryUser user = new LibraryUser();
        user.setUsername("John Doe");
        user.setPassword("haha123");
        LibraryUser savedUser = userRepository.save(user);

        Rent rent = new Rent();
        rent.setUser(savedUser);
        rent.setBook(savedBook);
        rent.setStartDate(new Date());
        rent.setEndDate(new Date());
        rent.setStatus(false);
        Rent savedRent = rentRepository.save(rent);

        Assertions.assertThat(savedRent).isNotNull();
        Assertions.assertThat(savedRent.getId()).isGreaterThan(0);
        Assertions.assertThat(savedRent.getBook()).isEqualTo(savedBook);
        Assertions.assertThat(savedRent.getUser()).isEqualTo(savedUser);
    }
}
