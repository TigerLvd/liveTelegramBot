package model.homeGroups.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

import model.homeGroups.db.StatInfo;

public class StatInfoDaoServiceImpl extends AbstractHibernateDao<StatInfo> implements StatInfoDaoService {
    @Override
    public List<StatInfo> findAllByHomeGroupId(Long id) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "st");
        criteria.createAlias("st.homeGroup", "hg");
        criteria.add(Restrictions.eq("hg.id", id));
        criteria.addOrder(Order.asc("st.eventDate"));
        return criteria.list();
    }
}
