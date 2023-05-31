package repository;

import domain.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookRepository extends CrudRepository<Book, UUID> {

    Book findBookByIsbn13(String isbn13);

    int getStars(@Param("bookId") UUID bookId);
}
