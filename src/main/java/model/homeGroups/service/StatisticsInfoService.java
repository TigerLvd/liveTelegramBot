package model.homeGroups.service;

import model.homeGroups.db.StatisticsInfo;

import java.util.Collection;
import java.util.List;

public interface StatisticsInfoService {
    StatisticsInfo findById(Long id);

    void delete(StatisticsInfo info);

    void saveOrUpdate(Collection<StatisticsInfo> entities);

    void saveOrUpdate(StatisticsInfo info);

    List<StatisticsInfo> findByIds(Collection<Long> ids);
}
