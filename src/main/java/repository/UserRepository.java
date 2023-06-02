package repository;

import domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {

    List<UUID> getFavoriteBookIds(@Param("userId") String userId);

    User findUserByUsername(@Param("username") String username);

    Boolean getBookIsFavorited(@Param("bookId") UUID bookId, @Param("userId") UUID userId);

    Integer getFavoriteListSize(@Param("userId") UUID userId);
}
