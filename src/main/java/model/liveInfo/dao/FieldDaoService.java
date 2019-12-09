package model.liveInfo.dao;

import java.util.List;

import model.abstractdao.Dao;
import model.liveInfo.db.Field;

public interface FieldDaoService extends Dao<Field> {
    Field findByName(String text);

    List<Field> findByParentId(Long parentId);
}
