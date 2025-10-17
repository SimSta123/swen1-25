package at.technikum.application.common;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {

    Optional<T> find(String id);

    List<T> findAll();

    <T> void save(T object);

    // Todo update(Todo todo);

    String delete(String id);
}
