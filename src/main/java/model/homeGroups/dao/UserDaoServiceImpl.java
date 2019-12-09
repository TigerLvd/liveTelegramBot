package model.homeGroups.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import model.abstractdao.AbstractHibernateDao;
import model.homeGroups.db.User;

public class UserDaoServiceImpl extends AbstractHibernateDao<User> implements UserDaoService {
    @Override
    public User findByTelegramId(Long id) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "us");
        criteria.add(Restrictions.eq("us.telegramUserId", id));
        return (User) criteria.uniqueResult();
    }
}
