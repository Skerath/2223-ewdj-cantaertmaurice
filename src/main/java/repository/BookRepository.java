package repository;

import domain.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public interface BookRepository extends CrudRepository<Book, UUID> {

    Book findBookByIsbn13(String isbn13);

//    int getStars(@Param("bookId") UUID bookId);

    List<Object[]> getStarsForBooks();

    List<Book> findAllOrderedByBookId();

    List<Object[]> getTop10Books();
}
