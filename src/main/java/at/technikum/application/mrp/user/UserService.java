package at.technikum.application.mrp.user;

import at.technikum.application.todo.exception.EntityNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


public class UserService {

    private final UserRepositoryC userRepository;
    private List<User> users;

    public UserService(UserRepositoryC userRepositoryC) {
        this.userRepository = userRepositoryC;
        this.users = new ArrayList<>();
    }

    public User create(User user) {
        // is todo valid?
        user.setId(UUID.randomUUID().toString());
        users.add(user);
        user.setDone(true);

        return userRepository.save(user);
    }

    public User get(String id) {
        return userRepository.find(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User update(String id, User update) {
        User user = userRepository.find(id)
                .orElseThrow(EntityNotFoundException::new);

        user.setUsername(update.getUsername());
        user.setDone(update.isDone());

        return userRepository.save(user);
    }

    public User delete(String id) {
        return userRepository.delete(id);
    }

    //Hier neue Methode um zu schauen ob übergebene name und pw stimmen??
}

