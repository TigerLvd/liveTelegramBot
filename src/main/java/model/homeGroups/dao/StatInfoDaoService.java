package model.homeGroups.dao;

import java.util.Date;
import java.util.List;

import model.homeGroups.db.StatInfo;

public interface StatInfoDaoService extends Dao<StatInfo> {

    List<StatInfo> findAllByHomeGroupId(Long id);

    StatInfo findByDateAndHomeGroupId(Date date, Long id);
}
