package Aalst_Cantaert_Maurice;

import domain.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import repository.BookRepository;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
@RequestMapping("/")
@Slf4j
public class BooksListController {

    @Autowired
    BookRepository bookRepository;

    @GetMapping
    public String showBooks(@RequestParam(value = "success", required = false) Boolean gotAdded,
                            @RequestParam(value = "updated", required = false) Boolean gotUpdated,
                            Model model,
                            Authentication authentication) {
        List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        boolean isAdmin = roles.contains("ROLE_ADMIN");

        Iterable<Book> books = bookRepository.findAllBooksOrderedByName();
        Map<UUID, Long> starsPerBook = bookRepository.getStarsForBooks().
                stream()
                .collect(
                        Collectors.toMap(bookId -> (UUID) bookId[0], stars -> (Long) stars[1])
                );

        if (gotAdded != null)
            model.addAttribute("gotAdded", gotAdded ? "Book added to favorites." : "Book removed from favorites.");
        if (gotUpdated != null)
            model.addAttribute("gotUpdated", "Book has been updated.");
        model.addAttribute("booksList", books);
        model.addAttribute("starsPerBook", starsPerBook);
        model.addAttribute("isAdmin", isAdmin);
        return "booksList";
    }

    @GetMapping(value = "/popular")
    public String showPopularBooks(Model model, Authentication authentication) {
        List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        boolean isAdmin = roles.contains("ROLE_ADMIN");
        Iterable<Book> books = bookRepository.findAllBooksOrderedByName();
        Map<UUID, Long> starsPerBook = bookRepository.getStarsForBooks()
                .stream()
                .collect(
                        Collectors.toMap(bookId -> (UUID) bookId[0], stars -> (Long) stars[1])
                );

        List<Book> sortedBooks = StreamSupport.stream(books.spliterator(), false)
                .sorted(Comparator.comparing(book -> starsPerBook.getOrDefault(book.getBookId(), 0L), Comparator.reverseOrder()))
                .toList();
        model.addAttribute("booksList", sortedBooks);
        model.addAttribute("starsPerBook", starsPerBook);
        model.addAttribute("isAdmin", isAdmin);
        return "booksList";
    }

    @ModelAttribute("username")
    public String username(Principal principal) {
        return principal.getName();
    }
}