package model.homeGroups.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import model.homeGroups.dao.HomeGroupDaoService;
import model.homeGroups.db.HomeGroup;

public class HomeGroupServiceImpl implements HomeGroupService {
    HomeGroupDaoService dao;

    public HomeGroupDaoService getDao() {
        return dao;
    }

    @Autowired
    public void setDao(HomeGroupDaoService dao) {
        this.dao = dao;
    }

    @Override
    @Transactional(readOnly = true)
    public HomeGroup findById(Long id) {
        return getDao().findById(id);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(HomeGroup homeGroup) {
        getDao().delete(homeGroup);
    }

    @Override
    @Transactional(readOnly = false)
    public void saveOrUpdate(Collection<HomeGroup> homeGroups) {
        getDao().saveOrUpdate(homeGroups);
    }

    @Override
    @Transactional(readOnly = false)
    public void saveOrUpdate(HomeGroup homeGroup) {
        getDao().saveOrUpdate(homeGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HomeGroup> findByIds(Collection<Long> ids) {
        return getDao().findByIds(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HomeGroup> findAll(String... property) {
        if (property.length > 0) {
            return dao.findAll(property);
        } else {
            return dao.findAll();
        }
    }
}
