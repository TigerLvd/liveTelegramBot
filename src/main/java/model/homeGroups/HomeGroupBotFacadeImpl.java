package model.homeGroups;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.homeGroups.db.HomeGroup;
import model.homeGroups.db.StatInfo;
import model.homeGroups.db.User;
import model.homeGroups.service.HomeGroupService;
import model.homeGroups.service.StatInfoService;
import model.homeGroups.service.UserService;

public class HomeGroupBotFacadeImpl implements HomeGroupBotFacade {

    private UserService userService;

    private HomeGroupService homeGroupService;

    private StatInfoService statInfoService;

    private TelegramLongPollingBot bot;

    @Override
    public void sendUsersList(ReplyKeyboardMarkup keyboardMarkup, Long chatId) {
        List<User> allUsers = userService.findAll();
        StringBuilder userInfos = new StringBuilder();
        if (null != allUsers) {
            int i= 1;
            for (User usr : allUsers) {
                userInfos.append(i);
                userInfos.append(") ");
                userInfos.append("admin=");
                userInfos.append(usr.isAdmin());
                userInfos.append(", ");
                userInfos.append("id=");
                userInfos.append(usr.getId());
                userInfos.append(", chatId=");
                userInfos.append(usr.getTelegramUserId());
                userInfos.append(", firstName=");
                userInfos.append(usr.getFirstName());
                userInfos.append(", lastName=");
                userInfos.append(usr.getLastName());
                userInfos.append(", nickName=");
                userInfos.append(usr.getNickName());
                userInfos.append(", comment=");
                userInfos.append(usr.getComment());
                userInfos.append(", isLeader=");
                userInfos.append(usr.isLeader());
                userInfos.append("\n\n");
                i++;
            }
        }
        if (userInfos.length() == 0) {
            userInfos.append("пусто");
        }
        send(chatId, userInfos.toString(), keyboardMarkup);
    }

    @Override
    public void sendNewUsersList(ReplyKeyboardMarkup keyboardMarkup, Long chatId) {
        //todo переделать на новый сервис
        List<User> allUsers = userService.findAll();
        StringBuilder userInfos = new StringBuilder();
        if (null != allUsers) {
            int i= 1;
            for (User usr : allUsers) {
                if (usr.hasHomeGroup() || usr.isAdmin()) {
                    continue;
                }
                userInfos.append(i);
                userInfos.append(") ");
                userInfos.append("admin=");
                userInfos.append(usr.isAdmin());
                userInfos.append(", ");
                userInfos.append("id=");
                userInfos.append(usr.getId());
                userInfos.append(", chatId=");
                userInfos.append(usr.getTelegramUserId());
                userInfos.append(", firstName=");
                userInfos.append(usr.getFirstName());
                userInfos.append(", lastName=");
                userInfos.append(usr.getLastName());
                userInfos.append(", nickName=");
                userInfos.append(usr.getNickName());
                userInfos.append(", comment=");
                userInfos.append(usr.getComment());
                userInfos.append(", isLeader=");
                userInfos.append(usr.isLeader());
                userInfos.append("\n\n");
                i++;
            }
        }
        if (userInfos.length() == 0) {
            userInfos.append("пусто");
        }
        send(chatId, userInfos.toString(), keyboardMarkup);
    }

    @Override
    public void sendHomeGroupsList(Long chatId, ReplyKeyboardMarkup keyboardMarkup, Long adminId) {
        List<HomeGroup> groups = homeGroupService.findAll(HomeGroup.LIEDER_FIELD);
        StringBuilder groupsInfos = new StringBuilder();
        if (null != groups) {
            int i = 1;
            for (HomeGroup group : groups) {
                groupsInfos.append(i++);
                groupsInfos.append(") ");
                if (null != group.getLieder() && (null != group.getLieder().getLastName() || null != group.getLieder().getFirstName())) {
                    groupsInfos.append("лидер: ");
                    if (null != group.getLieder().getLastName()) {
                        groupsInfos.append(group.getLieder().getLastName());
                        groupsInfos.append(" ");
                    }
                    if (null != group.getLieder().getFirstName()) {
                        groupsInfos.append(group.getLieder().getFirstName());
                    }
                    groupsInfos.append(", ");
                }
                if (adminId.equals(chatId)) {
                    groupsInfos.append("(");
                    groupsInfos.append(group.getComment());
                    groupsInfos.append("), ");
                }
                if (null != group.getLiederPhoneNumber()) {
                    groupsInfos.append("номер лидера: ");
                    groupsInfos.append(formatPhoneNumber(group.getLiederPhoneNumber()));
                    groupsInfos.append(", ");
                }
                if (null != group.getLieder() && null != group.getLieder().getNickName()) {
                    groupsInfos.append("telegram: @");
                    groupsInfos.append(group.getLieder().getNickName());
                    groupsInfos.append(", ");
                }
                groupsInfos.append("адрес: ");
                groupsInfos.append(group.getAddress());
                groupsInfos.append(", время: ");
                groupsInfos.append(group.getDayOfTheWeek().getTitle());
                groupsInfos.append(" в ");
                groupsInfos.append(group.getTime());
                groupsInfos.append("\n\n");
            }
        }
        if (groupsInfos.length() == 0) {
            groupsInfos.append("пусто");
        }
        send(chatId, groupsInfos.toString(), keyboardMarkup);
    }

    @Override
    public void sendLostStatInfos(Long chatId, User user, ReplyKeyboardMarkup keyboardMarkup, Boolean sendEmpty) {
        List<Date> lostWeeks = getLostWeeks(user);

        if (lostWeeks.isEmpty()) {
            if (!sendEmpty) {
                return;
            }
            send(chatId, "Все данные введены, задолженностей нет", keyboardMarkup);
        } else {
            StringBuilder result = new StringBuilder("Нет информации за следующие недели:\n");
            int i = 1;
            for (Date monday : lostWeeks) {
                result.append(i);
                result.append(") ");
                result.append(getMndToSunString(monday));
                result.append("\n");

                i++;
            }

            send(chatId, result.toString(), keyboardMarkup);
        }
    }

    /**
     * @param monday понедельник, от которого идёт отсчёт.
     * @return строка в формате: "<понедельник>-<воскресенье>"
     */
    private String getMndToSunString(Date monday) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(monday);
        calendar.add(Calendar.DAY_OF_MONTH, 6);
        return getStringOfDate(monday) + "-" + getStringOfDate(calendar.getTime());
    }

    /**
     * @param user пользователь по которому ищется информация
     * @return список понедельников у недель, где не было внесено информации о статистике, начиная с даты
     * включения ячейки в систему.
     */
    private List<Date> getLostWeeks(User user) {
        Map<Date, StatInfo> statInfoByFirstDayOfWeek = getStatInfoByFirstDayOfWeek(user);
        Calendar start = getStartDate(user);
        List<Date> lostWeeks = new ArrayList<Date>();
        while (start.before(new GregorianCalendar())) {
            if (statInfoByFirstDayOfWeek.get(start.getTime()) == null) {
                lostWeeks.add(start.getTime());
            }
            start.add(Calendar.DAY_OF_MONTH, 7);
        }
        return lostWeeks;
    }

    @Override
    public void sendEnteredStatInfos(Long chatId, User user, ReplyKeyboardMarkup keyboardMarkup) {
        List<StatInfo> allStatInfos = statInfoService.findAllByHomeGroupId(user.getHomeGroup().getId());
        if (null != allStatInfos && !allStatInfos.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            int i = 1;
            stringBuilder.append("Введено:\n\n");
            for (StatInfo statInfo : allStatInfos) {
                stringBuilder.append(i);
                stringBuilder.append(") ");
                stringBuilder.append(statInfo.getCount());
                stringBuilder.append(" за ");
                stringBuilder.append(getStringOfDate(statInfo.getEventDate()));
                stringBuilder.append("\n\n");
                i++;
            }
            send(chatId, stringBuilder.toString(), keyboardMarkup);
        } else {
            send(chatId, "ничего не было введено", keyboardMarkup);
        }
    }

    private Map<Date, StatInfo> getStatInfoByFirstDayOfWeek(User user) {
        Map<Date, StatInfo> statInfoByFirstDayOfWeek = new HashMap<Date, StatInfo>();
        List<StatInfo> allStatInfos = statInfoService.findAllByHomeGroupId(user.getHomeGroup().getId());
        if (null != allStatInfos) {
            for (StatInfo statInfo : allStatInfos) {
                Date time = getFirstDayOfWeek(statInfo.getEventDate());
                statInfoByFirstDayOfWeek.put(time, statInfo);
            }
        }
        return statInfoByFirstDayOfWeek;
    }

    private Calendar getStartDate(User user) {
        Calendar start;
        if (user.getHomeGroup().getStartDate() != null) {
            start = new GregorianCalendar();
            start.setTime(user.getHomeGroup().getStartDate());
        } else {
            start = new GregorianCalendar();
        }

        start.setTime(getFirstDayOfWeek(start.getTime()));
        return start;
    }

    public StatInfo addStatInfo(Long chatId, User user, Date eventDate, Integer count) {
        StatInfo statInfo = statInfoService.findByDateAndHomeGroupId(eventDate, user.getHomeGroup().getId());
        if (statInfo == null) {
            statInfo = new StatInfo();
        }
        statInfo.setCount(count);
        statInfo.setEventDate(eventDate);
        statInfo.setSaveDate(new Timestamp(new Date().getTime()));
        statInfo.setSaverId(chatId.intValue());
        statInfo.setComment(user.getComment());
        statInfo.setHomeGroup(user.getHomeGroup());
        statInfoService.saveOrUpdate(statInfo);
        return statInfo;
    }

    public User addUser(Chat chat) {
        User user;
        user = new User();
        user.setFirstName(chat.getFirstName());
        user.setLastName(chat.getLastName());
        user.setNickName(chat.getUserName());
        user.setTelegramUserId(chat.getId());
        userService.saveOrUpdate(user);
        return user;
    }

    private Date getFirstDayOfWeek(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            dayOfWeek = 7;
        } else {
            dayOfWeek--;
        }
        calendar.add(Calendar.DAY_OF_MONTH, dayOfWeek * (-1) + 1); // получили понедельник
        return calendar.getTime();
    }

    private String getStringOfDate(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.YEAR);
    }

    private String formatPhoneNumber(String str) {
        StringBuilder number = new StringBuilder("+7 (");
        number.append(str.substring(0, 3));
        number.append(") ");
        number.append(str.substring(3, 6));
        number.append(" ");
        number.append(str.substring(6, 8));
        number.append(" ");
        number.append(str.substring(8, 10));
        return number.toString();
    }

    @Override
    public void send(Long chatId, String str, ReplyKeyboardMarkup keyboard) {
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(str)
                .setReplyMarkup(keyboard);
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(Long chatId, Integer messageId, String str, InlineKeyboardMarkup keyboard) {
        if (null == messageId) {
            SendMessage message = new SendMessage()
                    .setChatId(chatId)
                    .setText(str)
                    .setReplyMarkup(keyboard);
            try {
                bot.execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            return;
        }

        EditMessageText message = new EditMessageText()
                .setChatId(chatId)
                .setMessageId(messageId)
                .setText(str)
                .setReplyMarkup(keyboard);
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(Long chatId, String str) {
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(str);
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public HomeGroupService getHomeGroupService() {
        return homeGroupService;
    }

    public void setHomeGroupService(HomeGroupService homeGroupService) {
        this.homeGroupService = homeGroupService;
    }

    public StatInfoService getStatInfoService() {
        return statInfoService;
    }

    public void setStatInfoService(StatInfoService statInfoService) {
        this.statInfoService = statInfoService;
    }

    @Override
    public void setBot(TelegramLongPollingBot homeGroupBot) {
        this.bot = homeGroupBot;
    }

    @Override
    public void sendInfoAbout(Long chatId, Long userId, ReplyKeyboardMarkup keyboardMarkup) {
        User userInfo = userService.findById(userId);
        if (null != userInfo) {
            StringBuilder userInfos = new StringBuilder();
            userInfos.append("admin=");
            userInfos.append(userInfo.isAdmin());
            userInfos.append(", ");
            userInfos.append("id=");
            userInfos.append(userInfo.getId());
            userInfos.append(", chatId=");
            userInfos.append(userInfo.getTelegramUserId());
            userInfos.append(", firstName=");
            userInfos.append(userInfo.getFirstName());
            userInfos.append(", lastName=");
            userInfos.append(userInfo.getLastName());
            userInfos.append(", nickName=");
            userInfos.append(userInfo.getNickName());
            userInfos.append(", comment=");
            userInfos.append(userInfo.getComment());
            userInfos.append(", isLeader=");
            userInfos.append(userInfo.isLeader());
            if (userInfo.hasHomeGroup()) {
                userInfos.append("\n\nВведено статистики:\n\n");
                List<StatInfo> allStatInfos = statInfoService.findAllByHomeGroupId(userInfo.getHomeGroup().getId());
                if (null != allStatInfos && !allStatInfos.isEmpty()) {
                    int i = 1;
                    for (StatInfo statInfo : allStatInfos) {
                        userInfos.append(i);
                        userInfos.append(") ");
                        userInfos.append(statInfo.getCount());
                        userInfos.append(" за ");
                        userInfos.append(getStringOfDate(statInfo.getEventDate()));
                        userInfos.append("\n\n");
                        i++;
                    }
                } else {
                    userInfos.append("ничего не было введено");
                }
            }
            send(chatId, userInfos.toString(), keyboardMarkup);
        } else {
            send(chatId, "Нет пользователя с указанным id", keyboardMarkup);
        }
    }

    @Override
    public void sendAllLostStatInfo(Long chatId, ReplyKeyboardMarkup keyboardMarkup) {
        List<User> users = userService.findAll();
        StringBuilder result = new StringBuilder();
        for (User user : users) {
            if (user.hasHomeGroup()) {
                List<Date> lostWeeks = getLostWeeks(user);

                if (!lostWeeks.isEmpty()) {
                    result.append("У пользователя ");
                    result.append(user.getComment());
                    result.append(" нет информации за следующие недели:\n");
                    int i = 1;
                    for (Date monday : lostWeeks) {
                        result.append(i);
                        result.append(") ");
                        result.append(getMndToSunString(monday));
                        result.append("\n");

                        i++;
                    }
                    result.append("\n");
                }
            }
        }
        send(chatId, result.toString(), keyboardMarkup);
    }
}
