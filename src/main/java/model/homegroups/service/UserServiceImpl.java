package model.homegroups.service;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import model.homegroups.dao.UserDaoService;
import model.homegroups.db.User;
import org.telegram.telegrambots.meta.api.objects.Chat;

public class UserServiceImpl implements UserService {
    UserDaoService dao;

    public UserDaoService getDao() {
        return dao;
    }

    public void setDao(UserDaoService dao) {
        this.dao = dao;
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return getDao().findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByTelegramId(Long id) {
        return getDao().findByTelegramId(id);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(User user) {
        getDao().delete(user);
    }

    @Override
    @Transactional(readOnly = false)
    public void saveOrUpdate(Collection<User> entities) {
        getDao().saveOrUpdate(entities);
    }

    @Override
    @Transactional(readOnly = false)
    public void saveOrUpdate(User user) {
        getDao().saveOrUpdate(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findByIds(Collection<Long> ids) {
        return getDao().findByIds(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll(String... properies) {
        if (properies.length > 0) {
            return getDao().findAll(properies);
        } else {
            return getDao().findAll();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findNew() {
        return getDao().findNew();
    }

    @Override
    @Transactional(readOnly = false)
    public User addUser(Chat chat) {
        User user;
        user = new User();
        user.setFirstName(chat.getFirstName());
        user.setLastName(chat.getLastName());
        user.setNickName(chat.getUserName());
        user.setTelegramUserId(chat.getId());
        saveOrUpdate(user);
        return user;
    }
}
