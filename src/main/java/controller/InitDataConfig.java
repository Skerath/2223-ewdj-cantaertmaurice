package controller;

import domain.Author;
import domain.Book;
import domain.BookLocation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import repository.AuthorRepository;
import repository.BookLocationRepository;
import repository.BookRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class InitDataConfig implements CommandLineRunner {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookLocationRepository bookLocationRepository;
    @Autowired
    private AuthorRepository authorRepository;


    @Override
    public void run(String... args) {

        Author jp = new Author(null, "Jan", "Piere", new ArrayList<>());
        Author sk = new Author(null, "Stephen", "King", new ArrayList<>());
        Author ma = new Author(null, "Marcus", "Aurelius", new ArrayList<>(3));

        Book book = new Book(null, "Meditations", new ArrayList<>(3), "9781603093255", new BigDecimal("15.00"), 889, new ArrayList<>(3));

        BookLocation bookLocation = new BookLocation(null, book, 51, 52, "Philosophy");


        authorRepository.save(jp);
        authorRepository.save(sk);
        authorRepository.save(ma);
        book.addAuthor(ma);
        book.addBookLocation(bookLocation);
        bookRepository.save(book);
        bookLocationRepository.save(bookLocation);
    }

}