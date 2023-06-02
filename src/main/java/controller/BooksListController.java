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
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
@Slf4j
public class BooksListController {

    @Autowired
    BookRepository bookRepository;



    @GetMapping
    public String showBooks(Model model) {
        Iterable<Book> books = bookRepository.findAllOrderedByBookId();
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
        Map<Book, Long> books = bookRepository.getTop10Books().
                stream()
                .collect(
                        Collectors.toMap(book -> (Book) book[0], stars -> (Long) stars[1]) // TODO if time check this out
                );
        log.error(books.toString());
        Map<UUID, Long> starsPerBook = bookRepository.getStarsForBooks().
                stream()
                .collect(
                        Collectors.toMap(bookId -> (UUID) bookId[0], stars -> (Long) stars[1])
                );
//        Map<UUID, Long> top10UUIDs = starsPerBook.entrySet().stream()
//                .sorted(Map.Entry.<UUID, Long>comparingByValue().reversed())
//                .limit(10)
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        model.addAttribute("booksList", books.keySet());
        model.addAttribute("starsPerBook", starsPerBook);
        return "booksList";
    }

    @ModelAttribute("username")
    public String username(Principal principal) {
        return principal.getName();
    }
}