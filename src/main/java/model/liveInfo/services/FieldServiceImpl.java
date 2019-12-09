package model.liveInfo.services;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import model.liveInfo.dao.FieldDaoService;
import model.liveInfo.db.Field;

public class FieldServiceImpl implements FieldService {
    private FieldDaoService dao;

    public FieldDaoService getDao() {
        return dao;
    }

    public void setDao(FieldDaoService dao) {
        this.dao = dao;
    }

    @Override
    @Transactional(readOnly = true)
    public Field findById(Long id) {
        if (null == id) {
            return null;
        }
        return getDao().findById(id);
    }

    @Override
    @Transactional
    public void delete(Field field) {
        getDao().delete(field);
    }

    @Override
    @Transactional
    public void saveOrUpdate(Collection<Field> fields) {
        getDao().saveOrUpdate(fields);
    }

    @Override
    @Transactional
    public void saveOrUpdate(Field field) {
        getDao().saveOrUpdate(field);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Field> findByIds(Collection<Long> ids) {
        return getDao().findByIds(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Field> findAll(String... property) {
        return getDao().findAll(property);
    }

    @Override
    @Transactional(readOnly = true)
    public Field findByName(String name) {
        return getDao().findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Field> findByParentId(Long parentId) {
        return getDao().findByParentId(parentId);
    }
}
