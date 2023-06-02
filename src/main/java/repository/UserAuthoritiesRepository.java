package repository;

import domain.User;
import domain.UserAuthorities;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthoritiesRepository extends CrudRepository<UserAuthorities, User> {
}
