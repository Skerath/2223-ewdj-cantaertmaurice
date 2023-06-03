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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // TODO remove
        Book book = bookRepository.findBookByIsbn13(isbn13);
        User authenticationUser = (User) authentication.getPrincipal();
        domain.User user = userRepository.findUserByUsername(authenticationUser.getUsername());

        List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        boolean isAdmin = roles.contains("ROLE_ADMIN");

        if (book == null)
            return "redirect:/404";

        boolean isFavorited = userRepository.getBookIsFavorited(book.getBookId(), user.getUserId());
        boolean isLimited = userRepository.getFavoriteListSize(user.getUserId()) >= user.getFavoriteBooksLimit();
        int stars = bookRepository.getStarsForBook(book.getBookId());

        model.addAttribute("book", book);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isFavorited", isFavorited);
        model.addAttribute("stars", stars);
        model.addAttribute("isLimited", isLimited && !isFavorited);
        return "bookDetails";
    }

    @PostMapping(value = "/{isbn13}")
    public String modifyFavoriteStatus(Model model, @PathVariable String isbn13) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Book book = bookRepository.findBookByIsbn13(isbn13);
        User authenticationUser = (User) authentication.getPrincipal();
        domain.User user = userRepository.findUserByUsername(authenticationUser.getUsername());

        if (book == null)
            return "redirect:/404";

        boolean isFavorited = userRepository.getBookIsFavorited(book.getBookId(), user.getUserId());

        if (isFavorited)
            user.removeFavouriteBook(book);
        else
            user.addFavouriteBook(book);

        userRepository.save(user);

        return "redirect:/?success=" + !isFavorited;
    }

}
