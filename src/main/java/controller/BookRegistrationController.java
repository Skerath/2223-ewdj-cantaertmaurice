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
import org.springframework.web.bind.annotation.RequestParam;
import validator.AuthorValidator;
import validator.BookLocationValidator;
import validator.BookValidator;

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

        model.addAttribute("book", toAddBook); // TODO: booklocations & authors don't get their book. maybe jpa does this automatically?
        model.addAttribute("bookLocations", toAddBook.getBookLocations());
        model.addAttribute("authors", toAddBook.getAuthors());
        return "bookRegistrationForm";
    }

    @PostMapping
    public String processRegistration(@Valid Book registration, BindingResult result, @RequestParam(value = "addAuthor", required = false) boolean addAuthor, @RequestParam(value = "addLocation", required = false) boolean addLocation, Model model) {
        if (addAuthor) {
            registration.addAuthor(new Author());
            return "bookRegistrationForm";
        }
        if (addLocation) {
            registration.addBookLocation(new BookLocation());
            return "bookRegistrationForm";
        }

        bookValidator.validate(registration, result);

        for (BookLocation bookLocation : registration.getBookLocations())
            bookLocationValidator.validate(bookLocation, result);
        for (Author author : registration.getAuthors())
            authorValidator.validate(author, result);

        if (result.hasErrors())
            return "bookRegistrationForm";

        return "redirect:/book/" + registration.getIsbn13();
    }
}
