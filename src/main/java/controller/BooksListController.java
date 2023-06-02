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
    public String showBooks(Model model, Authentication authentication) {
        List<Book> books = bookRepository.findAllOrderedByBookId();
//        List<Object[]> starsPerBook = bookRepository.getStarsForBooks();
//        Map<UUID, Long> starsPerBookParsed = starsPerBook.stream()
//                .collect(Collectors.toMap(bookId -> (UUID) bookId[0], stars -> (Long) stars[1]));

        Map<UUID, Long> starsPerBook = bookRepository.getStarsForBooks().
                stream()
                .collect(
                        Collectors.toMap(bookId -> (UUID) bookId[0], stars -> (Long) stars[1])
                );

//        log.error(starsPerBook.get(0)[0].toString());
//        log.error(starsPerBook.get(0)[1].toString());
//        log.error(String.valueOf(starsPerBookParsed.get(starsPerBook.get(0)[0])));
//        log.error(String.valueOf(starsPerBookParsed.get(UUID.randomUUID())));

        model.addAttribute("booksList", books);
        model.addAttribute("starsPerBook", starsPerBook);
//        model.addAttribute("bookLocations", toAddBook.getBookLocations());
//        model.addAttribute("authors", toAddBook.getAuthors());
        return "booksList";
    }

    @ModelAttribute("username")
    public String username(Principal principal) {
        return principal.getName();
    }
}