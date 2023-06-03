package Aalst_Cantaert_Maurice;

import domain.Author;
import domain.Book;
import domain.BookLocation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import repository.AuthorRepository;
import repository.BookLocationRepository;
import repository.BookRepository;
import validator.AuthorValidator;
import validator.BookLocationValidator;
import validator.BookValidator;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/registerbook")
@Slf4j
public class BookRegistrationController { // TODO merge this with editting
    @Autowired
    private BookLocationRepository bookLocationRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookValidator bookValidator;

    @Autowired
    private BookLocationValidator bookLocationValidator;

    @Autowired
    private AuthorValidator authorValidator;

    @GetMapping
    public String showRegistration(Model model, @ModelAttribute("bookId") String uuid, Authentication authentication) {
        List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        Book toAddBook = new Book();

        model.addAttribute("book", toAddBook);
        model.addAttribute("bookLocations", toAddBook.getBookLocations());
        model.addAttribute("authors", toAddBook.getAuthors());
        model.addAttribute("isAdmin", roles.contains("ROLE_ADMIN"));
        return "bookForm";
    }

    @PostMapping()
    public String processRegistration(@Valid Book registration, BindingResult result,
                                      @RequestParam(value = "addAuthor", required = false) boolean addAuthor,
                                      @RequestParam(value = "addLocation", required = false) boolean addLocation,
                                      @RequestParam(value = "removeAuthor", required = false) Integer authorNumber,
                                      @RequestParam(value = "removeLocation", required = false) Integer locationNumber,
                                      Authentication authentication,
                                      Model model) {
        List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        model.addAttribute("isAdmin", roles.contains("ROLE_ADMIN"));
        if (addAuthor) {
            registration.addAuthor(new Author());
            return "bookForm";
        }
        if (addLocation) {
            registration.addBookLocation(new BookLocation());
            return "bookForm";
        }

        if (authorNumber != null) {
            registration.removeAuthor(registration.getAuthors().get(authorNumber));
            return "bookForm";
        }

        if (locationNumber != null) {
            registration.removeBookLocation(registration.getBookLocations().get(locationNumber));
            return "bookForm";
        }

        for (BookLocation bookLocation : registration.getBookLocations()) {
            bookLocationValidator.validate(bookLocation, result);
            bookLocation.setBook(registration);
        }
        for (Author author : registration.getAuthors()) {
            authorValidator.validate(author, result);
            author.addBook(registration);
        }

        bookValidator.validate(registration, result);

        if (result.hasErrors())
            return "bookForm";

        authorRepository.saveAll(registration.getAuthors());
        bookRepository.save(registration);
        bookLocationRepository.saveAll(registration.getBookLocations());

        return "redirect:/book/" + registration.getIsbn13();
    }

    @ModelAttribute("username")
    public String username(Principal principal) {
        return principal.getName();
    }
}
