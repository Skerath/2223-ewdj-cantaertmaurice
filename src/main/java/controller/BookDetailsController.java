package controller;

import domain.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/book")
@Slf4j
public class BookDetailsController {

    @GetMapping(value = "/{isbn13}")
    public String showBook(Model model, @PathVariable String isbn13) {
        log.error(isbn13);
        Book toAddBook = new Book("Boeeeek");

        model.addAttribute("book", toAddBook);
//        model.addAttribute("bookLocations", toAddBook.getBookLocations());
//        model.addAttribute("authors", toAddBook.getAuthors());
        return "bookDetails";
    }

}
