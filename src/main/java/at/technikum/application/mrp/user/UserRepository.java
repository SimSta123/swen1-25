package at.technikum.application.mrp.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> find(String id);

    List<User> findAll();

    User save(User user);

    // Todo update(Todo todo);

    User delete(int id);
}
