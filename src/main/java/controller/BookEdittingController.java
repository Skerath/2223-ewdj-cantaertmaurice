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

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/update")
@Slf4j
public class BookEdittingController {

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

    @GetMapping(value = "/{isbn13}")
    public String showUpdate(Model model,
                             @PathVariable String isbn13) {
        try {
            if (isbn13.isBlank())
                throw new NoSuchElementException();
            Book toUpdateBook = bookRepository.findBookByIsbn13(isbn13);
            model.addAttribute("book", toUpdateBook);
            model.addAttribute("bookLocations", toUpdateBook.getBookLocations());
            model.addAttribute("authors", toUpdateBook.getAuthors());
            return "bookRegistrationForm";
        } catch (NoSuchElementException exception) {
            return "redirect:/registerbook";
        }
    }

    @PostMapping(value = "/{isbn13}")
    public String updateRegistration(@Valid Book registration, BindingResult result,
                                     @RequestParam(value = "addAuthor", required = false) boolean addAuthor,
                                     @RequestParam(value = "addLocation", required = false) boolean addLocation
    ) {
        if (addAuthor) {
            registration.addAuthor(new Author());
            return "bookRegistrationForm";
        }
        if (addLocation) {
            registration.addBookLocation(new BookLocation());
            return "bookRegistrationForm";
        }
        Book toUpdateBook = bookRepository.findById(registration.getBookId()).get();

        log.error(registration.toString());

        List<BookLocation> toUpdateLocations = new ArrayList<>();
        List<BookLocation> newLocations = new ArrayList<>();
        for (BookLocation bookLocation : registration.getBookLocations()) {
            bookLocationValidator.validate(bookLocation, result);
            if (bookLocation.getLocationId() == null) {
                log.error(bookLocation.toString());
                bookLocation.setBook(registration);
                newLocations.add(bookLocation);
            } else {
                log.error(String.valueOf(bookLocation.getBook()));
                toUpdateLocations.add(bookLocation);
            }
        }

        List<Author> toUpdateAuthors = new ArrayList<>();
        List<Author> newAuthors = new ArrayList<>();
        for (Author author : registration.getAuthors()) {
            authorValidator.validate(author, result);
            log.error(author.toString());
            if (author.getAuthorId() == null) {
                author.addBook(registration);
                newAuthors.add(author);
            } else {
                log.error(String.valueOf(author.getBooks()));
                toUpdateAuthors.add(author);
            }
            author.addBook(registration);
        }

        bookValidator.validate(registration, result);

        if (result.hasErrors())
            return "bookRegistrationForm";


        toUpdateBook.setIsbn13(registration.getIsbn13());
        toUpdateBook.setName(registration.getName());
        toUpdateBook.getBookLocations().clear();
        toUpdateBook.getBookLocations().addAll(toUpdateLocations);
        toUpdateBook.getAuthors().clear();
        toUpdateBook.getAuthors().addAll(toUpdateAuthors);

        authorRepository.saveAll(toUpdateAuthors);
        Book updatedBook = bookRepository.save(toUpdateBook);
        bookLocationRepository.saveAll(toUpdateLocations);

        return "redirect:/book/" + updatedBook.getIsbn13();
    }
}
