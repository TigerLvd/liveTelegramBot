package model.homegroups.service;

import java.util.Collection;
import java.util.List;

import model.homegroups.db.User;
import org.telegram.telegrambots.meta.api.objects.Chat;

public interface UserService {
    User findById(Long id);

    User findByTelegramId(Long id);

    void delete(User info);

    void saveOrUpdate(Collection<User> entities);

    void saveOrUpdate(User info);

    List<User> findByIds(Collection<Long> ids);

    List<User> findAll(String... properies);

    List<User> findNew();

    User addUser(Chat chat);
}
