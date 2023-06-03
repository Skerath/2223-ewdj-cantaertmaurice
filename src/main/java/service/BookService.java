package service;

import domain.Book;

import java.util.List;

public interface BookService {

    List<Book> getBooksFromAuthor(String authorId);

    Book getBookFromIsbn(String isbn);
}
