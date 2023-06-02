package controller;

import domain.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import repository.BookRepository;

import java.util.List;

@Controller
@RequestMapping("/book")
@Slf4j
public class BookDetailsController {

    @Autowired
    BookRepository bookRepository;

    @GetMapping(value = "/{isbn13}")
    public String showBook(Model model, @PathVariable String isbn13, Authentication authentication) {
        Book book = bookRepository.findBookByIsbn13(isbn13);

        List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        boolean isAdmin = roles.contains("ROLE_ADMIN");

        if (book == null)
            return "redirect:/404";

        model.addAttribute("book", book);
        model.addAttribute("isAdmin", isAdmin);
        return "bookDetails";
    }

}
