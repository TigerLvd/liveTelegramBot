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

    void updateParentId(Long id, Long parentId);

    void updateText(Long id, String textValue);

    void updateName(Long id, String nameValue);

    void updateLatitude(Long id, Float latitude);

    void updateLongitude(Long id, Float longitude);

    void updatePhotoPath(Long id, String photoPath);

    void updateColumnCount(Long id, Long columnCount);

    void updateIsShowBrothers(Long id, boolean isShowBrothers);
}
