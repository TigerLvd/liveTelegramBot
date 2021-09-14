package model.abstractdao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import utils.Utils;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class AbstractHibernateDao<T> implements Dao<T> {
    private SessionFactory sessionFactory;

    private Class<T> persistentClass;

    protected CriteriaBuilder getCriteriaBuilder() {
        return getSession().getCriteriaBuilder();
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

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
        if (Utils.isEmpty(entities)) {
            return;
        }

        for (T entity : entities) {
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
        return (T) getSession().get(getPersistentClass(), id); //todo добавить инициализацию
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
