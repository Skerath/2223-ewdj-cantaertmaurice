package service;

import domain.Book;
import dto.BookDTO;
import dto.BookLocationDTO;
import exception.AuthorNotFoundException;
import exception.BookNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.BookRepository;

import java.util.List;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;

    @Override
    public List<BookDTO> getBooksFromAuthorName(String authorName) {
        List<Book> result = bookRepository.findBooksByAuthorName(authorName);
        if (result.isEmpty())
            throw new AuthorNotFoundException(authorName);
        return result.stream().map(book -> new BookDTO(book, book.getBookLocations().stream().map(BookLocationDTO::new).toList())).toList();
    }

    @Override
    public BookDTO getBookFromIsbn(String isbn) {
        Book result = bookRepository.findBookByIsbn13(isbn);
        if (result == null)
            throw new BookNotFoundException(isbn);
        List<BookLocationDTO> bookLocationDTOs = result.getBookLocations().stream().map(BookLocationDTO::new).toList();
        return new BookDTO(result, bookLocationDTOs);
    }
}
