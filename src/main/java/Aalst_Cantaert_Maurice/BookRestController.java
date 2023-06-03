package Aalst_Cantaert_Maurice;

import dto.BookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.BookService;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class BookRestController {

    @Autowired
    private BookService bookService;

    @GetMapping(value = "/author/{authorId}")
    public List<BookDTO> getBooksFromAuthor(@PathVariable("authorId") String authorId) {
        return bookService.getBooksFromAuthorName(authorId);
    }

    @GetMapping(value = "/book/{isbn}")
    public BookDTO getEmployee(@PathVariable("isbn") String isbn) {
        return bookService.getBookFromIsbn(isbn);
    }
}