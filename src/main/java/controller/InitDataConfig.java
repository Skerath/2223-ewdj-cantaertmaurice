package controller;

import domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import repository.*;

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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAuthoritiesRepository authoritiesRepository;


    @Override
    public void run(String... args) {

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        Author jp = new Author(UUID.randomUUID(), "Jan", "Piere", new ArrayList<>());
        Author sk = new Author(UUID.randomUUID(), "Stephen", "King", new ArrayList<>());
        Author ma = new Author(UUID.randomUUID(), "Marcus", "Aurelius", new ArrayList<>(3));

        Book book = new Book(UUID.randomUUID(), "Meditations", new ArrayList<>(3), "9781603093255", new BigDecimal("15.00"), new ArrayList<>(3));
        Book noFavourites = new Book(UUID.randomUUID(), "It", new ArrayList<>(3), "9783453435773", null, new ArrayList<>(3));
        BookLocation bookLocationMA = new BookLocation(UUID.randomUUID(), book, 51, 52, "Philosophy");
        BookLocation bookLocationSK = new BookLocation(UUID.randomUUID(), noFavourites, 52, 53, "Horror");

        authorRepository.save(jp);
        authorRepository.save(sk);
        authorRepository.save(ma);
        book.addAuthor(ma);
        book.addBookLocation(bookLocationMA);
        bookRepository.save(book);
        noFavourites.addAuthor(sk);
        noFavourites.addBookLocation(bookLocationSK);
        bookRepository.save(noFavourites);
        bookLocationRepository.save(bookLocationMA);
        bookLocationRepository.save(bookLocationSK);

        ArrayList<Book> favouriteBooks = new ArrayList<>();
        favouriteBooks.add(book);


        User user = new User(UUID.randomUUID(), "Tessa", bCryptPasswordEncoder.encode("test"), 1, favouriteBooks, new ArrayList<>());
        UserAuthorities userAuthorities = new UserAuthorities(null, user, "ROLE_USER");
        User admin = new User(UUID.randomUUID(), "admin", bCryptPasswordEncoder.encode("admin"), 3, new ArrayList<>(), new ArrayList<>());
        UserAuthorities adminAuthorities = new UserAuthorities(null, admin, "ROLE_ADMIN");

        userRepository.save(user);
        authoritiesRepository.save(userAuthorities);
        userRepository.save(admin);
        authoritiesRepository.save(adminAuthorities);

    }
}