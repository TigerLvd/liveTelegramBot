package model.homeGroups.service;

import model.homeGroups.db.HomeGroup;
import model.homeGroups.db.User;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import model.homeGroups.dao.StatInfoDaoService;
import model.homeGroups.db.StatInfo;

public class StatInfoServiceImpl implements StatInfoService {
    StatInfoDaoService dao;

    public StatInfoDaoService getDao() {
        return dao;
    }

    public void setDao(StatInfoDaoService dao) {
        this.dao = dao;
    }

    @Override
    @Transactional(readOnly = true)
    public StatInfo findById(Long id) {
        return getDao().findById(id);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(StatInfo info) {
        getDao().delete(info);
    }

    @Override
    @Transactional(readOnly = false)
    public void saveOrUpdate(Collection<StatInfo> entities) {
        getDao().saveOrUpdate(entities);
    }

    @Override
    @Transactional(readOnly = false)
    public void saveOrUpdate(StatInfo info) {
        getDao().saveOrUpdate(info);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatInfo> findByIds(Collection<Long> ids) {
        return getDao().findByIds(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatInfo> findAll(String... properties) {
        if (properties.length > 0) {
            return getDao().findAll(properties);
        } else {
            return getDao().findAll();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatInfo> findAllByHomeGroupId(Long id) {
        return getDao().findAllByHomeGroupId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public StatInfo findByDateAndHomeGroupId(Date date, Long id) {
        return getDao().findByDateAndHomeGroupId(date, id);
    }

    @Override
    @Transactional
    public StatInfo addNewOrUpdate(Long chatId, User user, Date eventDate, Integer count) {
        StatInfo statInfo = findByDateAndHomeGroupId(eventDate, user.getHomeGroup().getId());
        if (statInfo == null) {
            statInfo = new StatInfo();
        }
        statInfo.setCount(count);
        statInfo.setEventDate(eventDate);
        statInfo.setSaveDate(new Timestamp(new Date().getTime()));
        statInfo.setSaverId(chatId.intValue());
        statInfo.setComment(user.getTelegramUserId().equals(chatId) ? user.getComment() : "root");
        statInfo.setHomeGroup(user.getHomeGroup());

        saveOrUpdate(statInfo);

        return statInfo;
    }

    @Override
    @Transactional
    public StatInfo addNewOrUpdate(Long chatId, HomeGroup homeGroup, String sender, Date eventDate, Integer count) {
        StatInfo statInfo = findByDateAndHomeGroupId(eventDate, homeGroup.getId());
        if (statInfo == null) {
            statInfo = new StatInfo();
        }
        statInfo.setCount(count);
        statInfo.setEventDate(eventDate);
        statInfo.setSaveDate(new Timestamp(new Date().getTime()));
        statInfo.setSaverId(chatId.intValue());
        statInfo.setComment(sender);
        statInfo.setHomeGroup(homeGroup);

        saveOrUpdate(statInfo);

        return statInfo;
    }
}
