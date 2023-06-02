package controller;

import domain.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import repository.BookRepository;
import repository.UserRepository;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/book")
@Slf4j
public class BookDetailsController {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping(value = "/{isbn13}")
    public String showBook(Model model, @PathVariable String isbn13) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Book book = bookRepository.findBookByIsbn13(isbn13);
        User authenticationUser = (User) authentication.getPrincipal();
        domain.User user = userRepository.findUserByUsername(authenticationUser.getUsername());

        List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        log.error(roles.toString());
        boolean isAdmin = roles.contains("ROLE_ADMIN");

        if (book == null)
            return "redirect:/404";

        boolean isFavorited = userRepository.getBookIsFavorited(book.getBookId(), user.getUserId());

        model.addAttribute("book", book);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isFavorited", isFavorited);
        return "bookDetails";
    }

    @PostMapping(value = "/{isbn13}")
    public String modifyFavoriteStatus(Model model, @PathVariable String isbn13) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Book book = bookRepository.findBookByIsbn13(isbn13);
        User authenticationUser = (User) authentication.getPrincipal();
        domain.User user = userRepository.findUserByUsername(authenticationUser.getUsername());

        List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        log.error(roles.toString());
        boolean isAdmin = roles.contains("ROLE_ADMIN");

        log.error("ping");

        if (book == null)
            return "redirect:/404";
        if (isAdmin)
            return "redirect:/403";

        boolean isFavorited = userRepository.getBookIsFavorited(book.getBookId(), user.getUserId());
        log.error(String.valueOf(isFavorited));

        if (isFavorited)
            user.removeFavouriteBook(book);
        else if (userRepository.getFavoriteListSize(user.getUserId()) < user.getFavoriteBooksLimit())
            user.addFavouriteBook(book);
        else
            return "redirect:/403";

        userRepository.save(user);

        return "redirect:/book/" + book.getIsbn13();
    }

}
