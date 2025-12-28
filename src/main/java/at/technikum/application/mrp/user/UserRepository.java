package at.technikum.application.mrp.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> find(int id);

    List<User> findAll();

    Optional<User> save(User user);

    // Todo update(Todo todo);

    User delete(String id);

    User update(User user);
}
