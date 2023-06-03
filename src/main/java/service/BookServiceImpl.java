package service;

import domain.Book;
import exception.AuthorNotFoundException;
import exception.BookNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import repository.AuthorRepository;
import repository.BookRepository;

import java.util.List;
import java.util.UUID;

@Component
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Override
    public List<Book> getBooksFromAuthor(String authorId) {
        List<Book> result = authorRepository.findBooksByAuthorId(UUID.fromString(authorId));
        if (result == null)
            throw new AuthorNotFoundException(UUID.fromString(authorId));
        return result;
    }

    @Override
    public Book getBookFromIsbn(String isbn) {
        Book result = bookRepository.findBookByIsbn13(isbn);
        if (result == null)
            throw new BookNotFoundException(isbn);
        return result;
    }
}
