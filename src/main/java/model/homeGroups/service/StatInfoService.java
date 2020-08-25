package model.homeGroups.service;

import model.homeGroups.db.StatInfo;
import model.homeGroups.db.User;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface StatInfoService {
    StatInfo findById(Long id);

    void delete(StatInfo info);

    void saveOrUpdate(Collection<StatInfo> entities);

    void saveOrUpdate(StatInfo info);

    List<StatInfo> findByIds(Collection<Long> ids);

    List<StatInfo> findAll(String... properties);

    List<StatInfo> findAllByHomeGroupId(Long id);

    StatInfo findByDateAndHomeGroupId(Date date, Long id);

    StatInfo addNewOrUpdate(Long chatId, User user, Date date, Integer count);
}
