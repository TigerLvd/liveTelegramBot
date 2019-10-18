package model.homeGroups.service;

import java.util.Collection;
import java.util.List;

import model.homeGroups.db.User;

public interface UserService {
    User findById(Long id);

    User findByTelegramId(Long id);

    void delete(User info);

    void saveOrUpdate(Collection<User> entities);

    void saveOrUpdate(User info);

    List<User> findByIds(Collection<Long> ids);

    List<User> findAll(String... properies);
}
