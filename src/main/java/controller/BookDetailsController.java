package controller;

import domain.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import repository.BookRepository;

@Controller
@RequestMapping("/book")
@Slf4j
public class BookDetailsController {

    @Autowired
    BookRepository bookRepository;

    @GetMapping(value = "/{isbn13}")
    public String showBook(Model model, @PathVariable String isbn13) {
        log.error(isbn13);

        Book book = bookRepository.findBookByIsbn13(isbn13);

        model.addAttribute("book", book);
//        model.addAttribute("bookLocations", toAddBook.getBookLocations());
//        model.addAttribute("authors", toAddBook.getAuthors());
        return "bookDetails";
    }

}
