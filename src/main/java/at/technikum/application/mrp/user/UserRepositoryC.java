package at.technikum.application.mrp.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryC implements UserRepository {

    @Override
    public Optional<User> find(String id) {
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        /*
        User user = new User();
        user.setId("1");
        List<User> users = new ArrayList<>();
        users.add(user);
        return users;
         */
        return List.of();
    }

    @Override
    public User save(User user) {
        return user;
    }

    @Override
    public User delete(int id) {
        return null;
    }
}
