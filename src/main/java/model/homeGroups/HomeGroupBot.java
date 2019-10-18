package model.homeGroups;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Date;
import java.util.List;

import model.homeGroups.db.HomeGroup;
import model.homeGroups.db.User;
import model.homeGroups.service.HomeGroupService;
import model.homeGroups.service.UserService;

@EnableScheduling
public class HomeGroupBot extends TelegramLongPollingBot {

    private final Long adminId;
    private final String botToken;
    private final String botName;

    UserService userService;
    HomeGroupService homeGroupService;

    public HomeGroupBot(String botToken, String botName, Long adminId, DefaultBotOptions options) {
        super(options);

        this.botToken = botToken;
        this.botName = botName;
        this.adminId = adminId;

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("application-context.xml");
        userService = (UserService) applicationContext.getBean("UserService");
        homeGroupService = (HomeGroupService) applicationContext.getBean("HomeGroupService");
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long userId = update.getMessage().getChat().getId();
            User user = userService.findByTelegramId(userId);

            Long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText();

            if (null == user) {
                if ("/start".equals(text)) {
                    send(chatId, "Привет, это бот для ввода инфорации о ячеках. Для дальнейшей работы введите пароль (пароль можно узнать у администратора).");
                } else if ("qwepswrd".equals(text)) {
                    user = new User();
                    user.setFirstName(update.getMessage().getChat().getFirstName());
                    user.setLastName(update.getMessage().getChat().getLastName());
                    user.setNickName(update.getMessage().getChat().getUserName());
                    user.setTelegramUserId(update.getMessage().getChat().getId());
                    userService.saveOrUpdate(user);
                    send(chatId, "Данные записаны: " + user.toString());
                    send(adminId, "Добавлен пользователь:" + user.toString());
                } else {
                    send(chatId, "Для дальнейшей работы введите пароль (пароль можно узнать у администратора).");
                }
                return;
            }

            if (null == user.getHomeGroup()) {
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
                String[] str = text.split(" ");
                Date date;
                try {
                    date = Date.valueOf(str[1]);
                } catch (Exception e) {
                    send(chatId, "Не верный формат! Ввести информацию в формате: Статистика: ДД.ММ.ГГ количество. Например:\nСтатистика: 01.02.19 987", keyboardMarkup);
                    return;
                }
                Long count;
                try {
                    count = new Long(str[2]);
                } catch (Exception e) {
                    send(chatId, "Не верный формат! Ввести информацию в формате: Статистика: дд.мм.гг КОЛИЧЕСТВО. Например:\nСтатистика: 01.02.19 987", keyboardMarkup);
                    return;
                }
                send(chatId, "Получено: " + count + " за " + date, keyboardMarkup);
                //todo доделать
                return;
            }

            if ("Не заполнено".equals(text)) {
                //todo доделать
                send(chatId, "Раздел в разработке", keyboardMarkup);
                return;
            }

            if ("Настройка уведомлений".equals(text)) {
                //todo доделать
                send(chatId, "Раздел в разработке", keyboardMarkup);
                return;
            }

            if ("Список ячеек".equals(text)) {
                List<HomeGroup> groups = homeGroupService.findAll(HomeGroup.LIEDER_FIELD);
                StringBuilder groupsInfos = new StringBuilder();
                if (null != groups) {
                    int i = 1;
                    for (HomeGroup group : groups) {
                        groupsInfos.append(i++);
                        groupsInfos.append(") ");
                        groupsInfos.append("адрес: ");
                        groupsInfos.append(group.getAddress());
                        groupsInfos.append(", номер лидера ячейки: ");
                        groupsInfos.append(formatPhoneNumber(group.getLiederPhoneNumber()));
                        if (null != group.getLieder()) {
                            groupsInfos.append(", tel: @");
                            groupsInfos.append(group.getLieder().getNickName());
                        }
                        groupsInfos.append(", ячейка обычно проходит в ");
                        groupsInfos.append(group.getDayOfTheWeek().getTitle());
                        groupsInfos.append(" в ");
                        groupsInfos.append(group.getTime());
                        groupsInfos.append("\n");
                    }
                }
                if (groupsInfos.length() == 0) {
                    groupsInfos.append("пусто");
                }
                send(chatId, groupsInfos.toString());
                return;
            }

            if (adminId.equals(user.getTelegramUserId())) {
                if ("Пользователи".equals(text)) {
                    List<User> allUsers = userService.findAll();
                    StringBuilder userInfos = new StringBuilder();
                    if (null != allUsers) {
                        for (User usr : allUsers) {
                            userInfos.append(usr.toString());
                            userInfos.append("\n");
                        }
                    }
                    if (userInfos.length() == 0) {
                        userInfos.append("пусто");
                    }
                    send(adminId, userInfos.toString(), keyboardMarkup);
                    return;
                }
            }

            send(chatId, "Привет, это бот для ввод инфорации о домашних группах.", keyboardMarkup);
        }
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
