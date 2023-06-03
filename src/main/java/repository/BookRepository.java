package repository;

import domain.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookRepository extends CrudRepository<Book, UUID> {

    Book findBookByIsbn13(String isbn13);

    Integer getStarsForBook(@Param("bookId") UUID bookId);

    List<Object[]> getStarsForBooks();

    List<Book> findAllBooksOrderedByName();

    List<Book> findBooksByAuthorName(@Param("authorName") String authorName);
}
