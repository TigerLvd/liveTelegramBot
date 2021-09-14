package model.homegroups.dao;

import model.abstractdao.Dao;
import model.homegroups.db.User;

import java.util.List;

public interface UserDaoService extends Dao<User> {

    User findByTelegramId(Long id);

    List<User> findNew();
}
