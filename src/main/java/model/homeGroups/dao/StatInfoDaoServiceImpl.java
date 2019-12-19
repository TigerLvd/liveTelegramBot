package model.homeGroups.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.List;

import model.abstractdao.AbstractHibernateDao;
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

    @Override
    public StatInfo findByDateAndHomeGroupId(Date eventDate, Long homeGroupId) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "st");
        criteria.createAlias("st.homeGroup", "hg");
        criteria.add(Restrictions.eq("hg.id", homeGroupId));
        criteria.add(Restrictions.eq("st.eventDate", eventDate));
        criteria.addOrder(Order.desc("st.saveDate"));
        criteria.setFirstResult(0);
        criteria.setMaxResults(1);
        return (StatInfo) criteria.uniqueResult();
    }
}
