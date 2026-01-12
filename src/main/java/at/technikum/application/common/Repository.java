package at.technikum.application.common;

import at.technikum.application.mrp.media.Media;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {

    Optional<T> find(T object);

    List<T> findAll();

    T save(T object);

    T delete(T object);

}
