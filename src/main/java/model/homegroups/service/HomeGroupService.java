package model.homegroups.service;

import java.util.Collection;
import java.util.List;

import model.homegroups.db.HomeGroup;

public interface HomeGroupService {
    HomeGroup findById(Long id);

    void delete(HomeGroup homeGroup);

    void saveOrUpdate(Collection<HomeGroup> homeGroups);

    void saveOrUpdate(HomeGroup homeGroup);

    List<HomeGroup> findByIds(Collection<Long> ids);

    List<HomeGroup> findAll(String... property);
}
