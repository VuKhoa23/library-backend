package com.library.Library;

import com.library.Library.entity.Book;
import com.library.Library.repository.BookRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@RunWith(SpringRunner.class)
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    public void SaveBook_ReturnValidId(){

        Book book = new Book();
        book.setCategory(null);
        book.setName("Harry Poteer");

        Book savedBook = bookRepository.save(book);

        Assertions.assertThat(savedBook).isNotNull();
        Assertions.assertThat(savedBook.getId()).isGreaterThan(0);
    }
}
