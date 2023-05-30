package controller;

import domain.Author;
import domain.Book;
import domain.BookLocation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import validator.AuthorValidator;
import validator.BookLocationValidator;
import validator.BookValidator;

import java.util.List;

@Controller
@RequestMapping("/registerbook")
@Slf4j
public class BookRegistrationController {

    @Autowired
    private BookValidator bookValidator;

    @Autowired
    private BookLocationValidator bookLocationValidator;

    @Autowired
    private AuthorValidator authorValidator;

    @GetMapping
    public String showRegistration(Model model) {

        Book toAddBook = new Book();

        toAddBook.addBookLocation(new BookLocation(toAddBook));
        toAddBook.addAuthor(new Author(List.of(toAddBook)));

        log.error(toAddBook.getBookLocations().toString());
        log.error(toAddBook.getAuthors().toString());

        model.addAttribute("book", toAddBook);
        model.addAttribute("bookLocations", toAddBook.getBookLocations());
        model.addAttribute("authors", toAddBook.getAuthors());
        return "bookRegistrationForm";
    }

    @PostMapping
    public String processRegistration(@Valid Book registration, BindingResult result, Model model) {
        for (BookLocation bookLocation : registration.getBookLocations())
            bookLocationValidator.validate(bookLocation, result);

        for (Author author : registration.getAuthors())
            authorValidator.validate(author, result);

        if (result.hasErrors()) {
            log.error(registration.toString());
            log.error(result.getAllErrors().toString());
            return "bookRegistrationForm";
        }
        String isbn13 = registration.getIsbn13();
        return "redirect:/book/" + isbn13;
    }
}
