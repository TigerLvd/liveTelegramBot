package model.homeGroups.dao;

import org.hibernate.Session;

import java.util.Collection;
import java.util.List;

public interface Dao<T>{
    Session getSession();

    Class<T> getPersistentClass();

    void setPersistentClass(Class<T> persistentClass);

    void delete(T entity);

    void saveOrUpdate(Collection<T> entities);

    void saveOrUpdate(T entity);

    T findById(Long id);

    List<T> findByIds(Collection<Long> ids);

    List<T> findAll(String... property);
}
