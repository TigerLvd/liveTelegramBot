package model.liveInfo.services;

import java.util.Collection;
import java.util.List;

import model.liveInfo.db.Field;

public interface FieldService {
    Field findById(Long id);

    void delete(Field field);

    void saveOrUpdate(Collection<Field> fields);

    void saveOrUpdate(Field field);

    List<Field> findByIds(Collection<Long> ids);

    List<Field> findAll(String... property);

    Field findByName(String name);

    List<Field> findByParentId(Long parentId);
}
