package controller;

import domain.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import repository.BookRepository;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
@RequestMapping("/")
@Slf4j
public class BooksListController {

    @Autowired
    BookRepository bookRepository;


    @GetMapping
    public String showBooks(Model model) {
        Iterable<Book> books = bookRepository.findAllBooksOrderedByName();
        Map<UUID, Long> starsPerBook = bookRepository.getStarsForBooks().
                stream()
                .collect(
                        Collectors.toMap(bookId -> (UUID) bookId[0], stars -> (Long) stars[1])
                );
        model.addAttribute("booksList", books);
        model.addAttribute("starsPerBook", starsPerBook);
        return "booksList";
    }

    @GetMapping(value = "/popular")
    public String showPopularBooks(Model model, Authentication authentication) {
        Iterable<Book> books = bookRepository.findAllBooksOrderedByName();
        Map<UUID, Long> starsPerBook = bookRepository.getBooksWithFavoriteCount()
                .stream()
                .collect(
                        Collectors.toMap(bookId -> (UUID) bookId[0], stars -> (Long) stars[1])
                );

        List<Book> sortedBooks = StreamSupport.stream(books.spliterator(), false)
                .sorted(Comparator.comparing(book -> starsPerBook.getOrDefault(book.getBookId(), 0L), Comparator.reverseOrder()))
                .toList();
        log.error(sortedBooks.toString());
        model.addAttribute("booksList", sortedBooks);
        model.addAttribute("starsPerBook", starsPerBook);
        return "booksList";
    }

    @ModelAttribute("username")
    public String username(Principal principal) {
        return principal.getName();
    }
}