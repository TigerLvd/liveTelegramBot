package model.liveInfo.services;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    @Transactional
    public void updateText(Long id, String textValue) {
        if (id == null) return;

        Field field = getDao().findById(id);
        if (field == null) return;

        field.setText(textValue);
        getDao().saveOrUpdate(field);
    }

    @Override
    @Transactional
    public void updateParentId(Long id, Long parentId) {
        if (id == null) return;

        Field field = getDao().findById(id);
        if (field == null) return;

        field.setParentId(parentId);
        getDao().saveOrUpdate(field);
    }

    @Override
    @Transactional
    public void updateName(Long id, String nameValue) {
        if (id == null) return;

        Field field = getDao().findById(id);
        if (field == null) return;

        field.setName(nameValue);
        getDao().saveOrUpdate(field);
    }

    @Override
    @Transactional
    public void updateLatitude(Long id, Float latitude) {
        if (id == null) return;

        Field field = getDao().findById(id);
        if (field == null) return;

        field.setLatitude(latitude);
        getDao().saveOrUpdate(field);
    }

    @Override
    @Transactional
    public void updateLongitude(Long id, Float longitude) {
        if (id == null) return;

        Field field = getDao().findById(id);
        if (field == null) return;

        field.setLongitude(longitude);
        getDao().saveOrUpdate(field);
    }

    @Override
    @Transactional
    public void updatePhotoPath(Long id, String photoPath) {
        if (id == null) return;

        Field field = getDao().findById(id);
        if (field == null) return;

        field.setPhotoPath(photoPath);
        getDao().saveOrUpdate(field);
    }

    @Override
    @Transactional
    public void updateColumnCount(Long id, Long columnCount) {
        if (id == null) return;

        Field field = getDao().findById(id);
        if (field == null) return;

        field.setColumnCount(columnCount);
        getDao().saveOrUpdate(field);
    }

    @Override
    @Transactional
    public void updateIsShowBrothers(Long id, boolean isShowBrothers) {
        if (id == null) return;

        Field field = getDao().findById(id);
        if (field == null) return;

        field.setShowBrothers(isShowBrothers);
        getDao().saveOrUpdate(field);
    }
}
