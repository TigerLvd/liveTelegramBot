package model.homeGroups.service;

import model.homeGroups.dao.Dao;
import model.homeGroups.db.StatisticsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public class StatisticsInfoServiceImpl implements StatisticsInfoService {
    Dao<StatisticsInfo> dao;

    public Dao<StatisticsInfo> getDao() {
        return dao;
    }

    @Autowired
    public void setDao(Dao<StatisticsInfo> dao) {
        this.dao = dao;
    }

    @Override
    @Transactional(readOnly = true)
    public StatisticsInfo findById(Long id) {
        return getDao().findById(id);
    }

    @Override
    public void delete(StatisticsInfo info) {
        getDao().delete(info);
    }

    @Override
    public void saveOrUpdate(Collection<StatisticsInfo> entities) {
        getDao().saveOrUpdate(entities);
    }

    @Override
    public void saveOrUpdate(StatisticsInfo info) {
        getDao().saveOrUpdate(info);
    }

    @Override
    public List<StatisticsInfo> findByIds(Collection<Long> ids) {
        return getDao().findByIds(ids);
    }
}
