package model.homeGroups.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class AbstractHibernateDao<T> implements Dao<T> {
    private SessionFactory sessionFactory;

    private Class<T> persistentClass;

    public AbstractHibernateDao() {
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    @Override
    public void setPersistentClass(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    @Override
    public void delete(T entity) {
        getSession().delete(entity);
    }

    @Override
    public void saveOrUpdate(Collection<T> entities) {
        if(CollectionUtils.isEmpty(entities)){
            return;
        }

        for(T entity : entities){
            getSession().saveOrUpdate(entity);
        }
    }

    @Override
    public void saveOrUpdate(T entity) {
        getSession().saveOrUpdate(entity);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T findById(Long id) {
        T t = (T) getSession().get(getPersistentClass(), id);
        //todo инициализация
        return t;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findByIds(Collection<Long> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        Criteria criteria = getSession().createCriteria(getPersistentClass());
        criteria.add(Restrictions.in("id", ids));

        return criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll(String... property) {
        Criteria criteria = getSession().createCriteria(getPersistentClass());
        for (String prop : property) {
            criteria.createAlias(prop, prop, JoinType.LEFT_OUTER_JOIN);
        }

        return criteria.list();
    }
}
