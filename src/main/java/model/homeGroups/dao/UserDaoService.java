package model.homeGroups.dao;

import model.homeGroups.db.User;

public interface UserDaoService extends Dao<User> {

    User findByTelegramId(Long id);
}
