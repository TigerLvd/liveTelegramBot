package model.liveInfo.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

import model.abstractdao.AbstractHibernateDao;
import model.liveInfo.db.Field;

public class FieldDaoServiceImpl extends AbstractHibernateDao<Field> implements FieldDaoService {
    @Override
    public Field findByName(String name) {
        Criteria criteria = getSession().createCriteria(getPersistentClass());
        criteria.add(Restrictions.eq("name", name));
        return (Field) criteria.uniqueResult();
    }

    @Override
    public List<Field> findByParentId(Long parentId) {
        Criteria criteria = getSession().createCriteria(getPersistentClass());
        criteria.add(Restrictions.eq("parentId", parentId));
        return criteria.list();
    }
}
