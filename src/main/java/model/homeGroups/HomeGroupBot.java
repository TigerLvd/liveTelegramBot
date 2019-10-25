package model.homeGroups;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
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

@EnableScheduling
public class HomeGroupBot extends TelegramLongPollingBot {

    private final Long adminId;
    private final String botToken;
    private final String botName;

    UserService userService;
    HomeGroupService homeGroupService;
    StatInfoService statInfoService;

    public HomeGroupBot(String botToken, String botName, Long adminId, DefaultBotOptions options) {
        super(options);

        this.botToken = botToken;
        this.botName = botName;
        this.adminId = adminId;

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("application-context.xml");
        userService = (UserService) applicationContext.getBean("UserService");
        homeGroupService = (HomeGroupService) applicationContext.getBean("HomeGroupService");
        statInfoService = (StatInfoService) applicationContext.getBean("StatInfoService");
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChat().getId();
            String text = update.getMessage().getText();

            User user = userService.findByTelegramId(chatId);

            if (null == user) {
                if ("/start".equals(text)) {
                    // когда зашёл первый раз и ввёл старт:
                    send(chatId, "Привет, это бот для ввода инфорации о ячеках. Для дальнейшей работы введите пароль (пароль можно узнать у администратора).");
                } else if ("qwepswrd".equals(text)) {
                    // когда первый раз успешно ввёл пароль:
                    user = addUser(update.getMessage().getChat());
                    send(chatId, "Данные записаны: " + user.toString());
                    send(adminId, "Добавлен пользователь:" + user.toString());
                } else {
                    // когда не успешно ввёл пароль:
                    send(chatId, "Для дальнейшей работы введите пароль (пароль можно узнать у администратора).");
                }
                return;
            }

            if (null == user.getHomeGroup()) {
                // когда ввёл пароль, но пользователя ещё не добавили в БД:
                send(chatId, "Пользователь был добавлен, информация обрабатывается, функционал пока не доступен.");
                return;
            }

            ReplyKeyboardMarkup keyboardMarkup;

            if (adminId.equals(user.getTelegramUserId())) {
                keyboardMarkup = new CustomKeyboardMarkup("Пользователи", "Ввод статистики", "Не заполнено", "Настройка уведомлений", "Список ячеек");
            } else {
                keyboardMarkup = new CustomKeyboardMarkup("Ввод статистики", "Не заполнено", "Настройка уведомлений", "Список ячеек");
            }

            if ("Ввод статистики".equals(text)) {
                send(chatId, "Введите информацию в формате: Статистика: дд.мм.гг количество. Например:\nСтатистика: 01.02.19 987", keyboardMarkup);
                return;
            }

            if (text.startsWith("Статистика: ")) {
                String[] str;
                Date date;
                try {
                    str = text.split(" ");
                    String[] sbStr = str[1].split("\\.");

                    date = getDate(sbStr[0], sbStr[1], sbStr[2]);
                } catch (Exception e) {
                    send(chatId, "Не верный формат! Ввести информацию в формате: Статистика: ДД.ММ.ГГ количество. Например:\nСтатистика: 01.02.19 987", keyboardMarkup);
                    return;
                }
                Integer count;
                try {
                    count = new Integer(str[2]);
                } catch (Exception e) {
                    send(chatId, "Не верный формат! Ввести информацию в формате: Статистика: дд.мм.гг КОЛИЧЕСТВО. Например:\nСтатистика: 01.02.19 987", keyboardMarkup);
                    return;
                }

                StatInfo statInfo = addStatInfo(chatId, user, date, count);

                send(chatId, "Получено: " + count + " за " + getStringOfDate(statInfo.getEventDate()), keyboardMarkup);
                return;
            }

            if ("Не заполнено".equals(text)) {
                sendLostStatInfos(chatId, user, keyboardMarkup);

                return;
            }

            if ("Настройка уведомлений".equals(text)) {
                //todo доделать
                send(chatId, "Раздел в разработке", keyboardMarkup);
                return;
            }

            if ("Список ячеек".equals(text)) {
                sendHomeGroupsList(chatId, keyboardMarkup);
                return;
            }

            if (adminId.equals(user.getTelegramUserId())) {
                if ("Пользователи".equals(text)) {
                    sendUsersList(keyboardMarkup);
                    return;
                }
            }

            send(chatId, "Привет, это бот для ввод инфорации о домашних группах.", keyboardMarkup);
        }
    }

    private void sendUsersList(ReplyKeyboardMarkup keyboardMarkup) {
        List<User> allUsers = userService.findAll();
        StringBuilder userInfos = new StringBuilder();
        if (null != allUsers) {
            int i= 1;
            for (User usr : allUsers) {
                userInfos.append(i);
                userInfos.append(") ");
                if (usr.isAdmin()) {
                    userInfos.append("admin=");
                    userInfos.append(usr.isAdmin());
                    userInfos.append(", ");
                }
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
                userInfos.append("\n\n");
            }
        }
        if (userInfos.length() == 0) {
            userInfos.append("пусто");
        }
        send(adminId, userInfos.toString(), keyboardMarkup);
    }

    private void sendHomeGroupsList(Long chatId, ReplyKeyboardMarkup keyboardMarkup) {
        List<HomeGroup> groups = homeGroupService.findAll(HomeGroup.LIEDER_FIELD);
        StringBuilder groupsInfos = new StringBuilder();
        if (null != groups) {
            int i = 1;
            for (HomeGroup group : groups) {
                groupsInfos.append(i++);
                groupsInfos.append(")");
                if (adminId.equals(chatId)) {
                    groupsInfos.append(" лидер: ");
                    groupsInfos.append(group.getComment());
                }
                groupsInfos.append(", адрес: ");
                groupsInfos.append(group.getAddress());
                if (null != group.getLiederPhoneNumber()) {
                    groupsInfos.append(", номер лидера ячейки: ");
                    groupsInfos.append(formatPhoneNumber(group.getLiederPhoneNumber()));
                }
                if (null != group.getLieder()) {
                    groupsInfos.append(", telegram: @");
                    groupsInfos.append(group.getLieder().getNickName());
                }
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

    private void sendLostStatInfos(Long chatId, User user, ReplyKeyboardMarkup keyboardMarkup) {
        Map<Date, StatInfo> statInfoByFirstDayOfWeek = getStatInfoByFirstDayOfWeek(user);
        Calendar start = getStartDate(user);
        List<Date> lostWeeks = new ArrayList<Date>();
        while (start.before(new GregorianCalendar())) {
            if (statInfoByFirstDayOfWeek.get(start.getTime()) == null) {
                lostWeeks.add(start.getTime());
            }
            start.add(Calendar.DAY_OF_MONTH, 7);
        }

        if (lostWeeks.isEmpty()) {
            send(chatId, "Все данные введены, задолженностей нет", keyboardMarkup);
        } else {
            StringBuilder stringBuilder = new StringBuilder("Нет информации за следующие недели:\n");
            int i = 1;
            for (Date monday : lostWeeks) {
                stringBuilder.append(i);
                stringBuilder.append(") ");
                stringBuilder.append(getStringOfDate(monday));
                stringBuilder.append("-");

                Calendar calendar = new GregorianCalendar();
                calendar.setTime(monday);
                calendar.add(Calendar.DAY_OF_MONTH, 6);
                stringBuilder.append(getStringOfDate(calendar.getTime()));
                stringBuilder.append("\n");

                i++;
            }

            send(chatId, stringBuilder.toString(), keyboardMarkup);
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

    private StatInfo addStatInfo(Long chatId, User user, Date eventDate, Integer count) {
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

    private User addUser(Chat chat) {
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
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        } else {
            dayOfWeek--;
        }
        calendar.add(Calendar.DAY_OF_MONTH, dayOfWeek * (-1) + 1); // получили понедельник
        return calendar.getTime();
    }

    private Date getDate(String dayString, String mounthString, String yearString) {
        Date date;
        Integer day = new Integer(dayString);
        Integer mounth = new Integer(mounthString) - 1;
        int year = yearString.length() == 4 ? new Integer(yearString) : new Integer(yearString) + 2000;

        Calendar calendar = new GregorianCalendar(year, mounth , day);
        date = calendar.getTime();
        return date;
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

    private void send(Long chatId, String str, ReplyKeyboardMarkup keyboard) {
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(str)
                .setReplyMarkup(keyboard);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void send(Long chatId, String str) {
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(str);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedRate = 5000)
    public void checkStat() {
        send(adminId, "проверка!");
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
