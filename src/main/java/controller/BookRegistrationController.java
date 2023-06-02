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
import org.springframework.web.bind.annotation.*;
import repository.AuthorRepository;
import repository.BookLocationRepository;
import repository.BookRepository;
import validator.AuthorValidator;
import validator.BookLocationValidator;
import validator.BookValidator;

@Controller
@RequestMapping("/registerbook")
@Slf4j
public class BookRegistrationController {
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
    public String showRegistration(Model model, @ModelAttribute("bookId") String uuid) {
        Book toAddBook = new Book();

        model.addAttribute("book", toAddBook);
        model.addAttribute("bookLocations", toAddBook.getBookLocations());
        model.addAttribute("authors", toAddBook.getAuthors());
        return "bookRegistrationForm";
    }

    @PostMapping()
    public String processRegistration(@Valid Book registration, BindingResult result,
                                      @RequestParam(value = "addAuthor", required = false) boolean addAuthor,
                                      @RequestParam(value = "addLocation", required = false) boolean addLocation) {
        if (addAuthor) {
            registration.addAuthor(new Author());
            return "bookRegistrationForm";
        }
        if (addLocation) {
            registration.addBookLocation(new BookLocation());
            return "bookRegistrationForm";
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
            return "bookRegistrationForm";

        authorRepository.saveAll(registration.getAuthors());
        bookRepository.save(registration);
        bookLocationRepository.saveAll(registration.getBookLocations());

        return "redirect:/book/" + registration.getIsbn13();
    }
}
