package services;

public interface userService<T> {
    T findById(Long userId);
}
