package repository;

import domain.BookLocation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookLocationRepository  extends CrudRepository<BookLocation, UUID> {
}
