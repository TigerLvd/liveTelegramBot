package model.homegroups.dao;

import model.abstractdao.AbstractHibernateDao;
import model.homegroups.db.HomeGroup;
import model.homegroups.db.User;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.persistence.criteria.*;
import java.util.List;

public class UserDaoServiceImpl extends AbstractHibernateDao<User> implements UserDaoService {
    @Override
    public User findByTelegramId(Long id) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "us");
        criteria.add(Restrictions.eq("us.telegramUserId", id));
        return (User) criteria.uniqueResult();
    }

    @Override
    public List<User> findNew() {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<User> qr = cb.createQuery(User.class);
        Root<User> root = qr.from(User.class);
        Join<User, HomeGroup> join = root.join("homeGroup", JoinType.INNER);

        qr.select(root)
                .where(
                        cb.and(
                                cb.isFalse(root.get("admin")),
                                cb.isNull(join)
                        ));
        return getSession().createQuery(qr).list();
    }
}
