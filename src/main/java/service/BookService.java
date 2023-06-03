package service;

import dto.BookDTO;

import java.util.List;

public interface BookService {

    List<BookDTO> getBooksFromAuthorName(String authorId);

    BookDTO getBookFromIsbn(String isbn);
}
