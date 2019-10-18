package model.homeGroups.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import model.homeGroups.dao.UserDaoService;
import model.homeGroups.db.User;

public class UserServiceImpl implements UserService {
    UserDaoService dao;

    public UserDaoService getDao() {
        return dao;
    }

    @Autowired
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
}
