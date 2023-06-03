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
            if (isbn13.isBlank())
                return "redirect:/404";

            Book toUpdateBook = bookRepository.findBookByIsbn13(isbn13);

            if (toUpdateBook == null)
                return "redirect:/404";

            model.addAttribute("book", toUpdateBook);
            model.addAttribute("bookLocations", toUpdateBook.getBookLocations());
            model.addAttribute("authors", toUpdateBook.getAuthors());
            return "bookForm";
    }

    @PostMapping(value = "/{isbn13}")
    public String updateRegistration(@Valid Book registration, BindingResult result,
                                     @RequestParam(value = "addAuthor", required = false) boolean addAuthor,
                                     @RequestParam(value = "addLocation", required = false) boolean addLocation,
                                     @RequestParam(value = "removeAuthor", required = false) Integer authorNumber,
                                     @RequestParam(value = "removeLocation", required = false) Integer locationNumber
    ) {
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

        Book toUpdateBook = bookRepository.findById(registration.getBookId()).orElse(null);

        if (toUpdateBook == null)
            return "redirect:/404";

        List<BookLocation> newBookLocations = new ArrayList<>();
        List<BookLocation> existingBookLocations = new ArrayList<>();
        for (BookLocation bookLocation : registration.getBookLocations()) {
            bookLocationValidator.validate(bookLocation, result);
            BookLocation correspondingLocationFromDb = bookLocationRepository.findById(bookLocation.getLocationId()).orElse(null);
            if (correspondingLocationFromDb == null) {
                bookLocation.setBook(registration);
                newBookLocations.add(bookLocation);
            } else {
                existingBookLocations.add(bookLocation);
            }
        }

        List<Author> newAuthors = new ArrayList<>();
        List<Author> existingAuthors = new ArrayList<>();
        for (Author author : registration.getAuthors()) {
            authorValidator.validate(author, result);
            Author correspondingAuthorFromDb = authorRepository.findById(author.getAuthorId()).orElse(null);
            if (correspondingAuthorFromDb == null) {
                newAuthors.add(author);
            } else {
                existingAuthors.add(author);
            }
        }

        bookValidator.validate(registration, result);


        if (result.hasErrors())
            return "bookForm";

        authorRepository.saveAll(existingAuthors);
        bookLocationRepository.saveAll(existingBookLocations);

        toUpdateBook.setIsbn13(registration.getIsbn13());
        toUpdateBook.setName(registration.getName());

        toUpdateBook.getAuthors().clear();
        authorRepository.saveAll(newAuthors);
        toUpdateBook.getAuthors().addAll(newAuthors);
        toUpdateBook.getAuthors().addAll(existingAuthors);

        toUpdateBook.getBookLocations().clear();
        bookLocationRepository.saveAll(newBookLocations);
        toUpdateBook.getBookLocations().addAll(newBookLocations);
        toUpdateBook.getBookLocations().addAll(existingBookLocations);

        Book updatedBook = bookRepository.save(toUpdateBook);
        return "redirect:/?updated=true";
    }
}
