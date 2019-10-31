package model.homeGroups;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import model.homeGroups.db.StatInfo;
import model.homeGroups.db.User;
import model.homeGroups.service.UserService;

public class HomeGroupBot extends TelegramLongPollingBot {

    private final Long adminId;
    private final String botToken;
    private final String botName;

    private HomeGroupBotFacade homeGroupBotFacade;
    private UserService userService;

    public HomeGroupBot(String botToken, String botName, Long adminId, DefaultBotOptions options) {
        super(options);

        this.botToken = botToken;
        this.botName = botName;
        this.adminId = adminId;

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("application-context.xml");
        userService = (UserService) applicationContext.getBean("UserService");
        homeGroupBotFacade = (HomeGroupBotFacade) applicationContext.getBean("HomeGroupBotFacade");
        homeGroupBotFacade.setBot(this);
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
                    String msg = "Привет, это бот для ввода инфорации о ячеках. Для дальнейшей работы введите пароль (пароль можно узнать у администратора).";
                    homeGroupBotFacade.send(chatId, msg);
                } else if ("qwepswrd".equals(text)) {
                    // когда первый раз успешно ввёл пароль:
                    user = homeGroupBotFacade.addUser(update.getMessage().getChat());
                    homeGroupBotFacade.send(chatId, "Данные записаны: " + user.toString());
                    homeGroupBotFacade.send(adminId, "Добавлен пользователь:" + user.toString());
                } else {
                    // когда не успешно ввёл пароль:
                    homeGroupBotFacade.send(chatId, "Для дальнейшей работы введите пароль (пароль можно узнать у администратора).");
                }
                return;
            }

            if (null == user.getHomeGroup()) {
                // когда ввёл пароль, но пользователя ещё не добавили в БД:
                homeGroupBotFacade.send(chatId, "Пользователь был добавлен, информация обрабатывается, функционал пока не доступен.");
                return;
            }

            ReplyKeyboardMarkup keyboardMarkup;

            if (user.isAdmin()) {
                keyboardMarkup = new CustomKeyboardMarkup("Пользователи", "Ввод статистики", "Не заполнено", "Настройка уведомлений", "Список ячеек");
            } else {
                keyboardMarkup = new CustomKeyboardMarkup("Ввод статистики", "Не заполнено", "Настройка уведомлений", "Список ячеек");
            }

            if ("Ввод статистики".equals(text)) {
                String msg = "Введите информацию в формате: Статистика: дд.мм.гг количество. Например:\nСтатистика: 01.02.19 987";
                homeGroupBotFacade.send(chatId, msg, keyboardMarkup);
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
                    String msg = "Не верный формат! Ввести информацию в формате: Статистика: ДД.ММ.ГГ количество. Например:\nСтатистика: 01.02.19 987";
                    homeGroupBotFacade.send(chatId, msg, keyboardMarkup);
                    return;
                }
                Integer count;
                try {
                    count = new Integer(str[2]);
                } catch (Exception e) {
                    String msg = "Не верный формат! Ввести информацию в формате: Статистика: дд.мм.гг КОЛИЧЕСТВО. Например:\nСтатистика: 01.02.19 987";
                    homeGroupBotFacade.send(chatId, msg, keyboardMarkup);
                    return;
                }

                StatInfo statInfo = homeGroupBotFacade.addStatInfo(chatId, user, date, count);

                homeGroupBotFacade.send(chatId, "Получено: " + count + " за " + getStringOfDate(statInfo.getEventDate()), keyboardMarkup);
                return;
            }

            if ("Не заполнено".equals(text)) {
                homeGroupBotFacade.sendLostStatInfos(chatId, user, keyboardMarkup, true);
                return;
            }

            if ("Настройка уведомлений".equals(text)) {
                sendNotificationSettings(chatId, user, null);
                return;
            }

            if ("Список ячеек".equals(text)) {
                homeGroupBotFacade.sendHomeGroupsList(chatId, keyboardMarkup, adminId);
                return;
            }

            if (adminId.equals(user.getTelegramUserId())) {
                if ("Пользователи".equals(text)) {
                    homeGroupBotFacade.sendUsersList(keyboardMarkup, adminId);
                    return;
                }
            }

            homeGroupBotFacade.send(chatId, "Привет, это бот для ввод инфорации о домашних группах.", keyboardMarkup);
        } else if (update.hasCallbackQuery()) {
            String callData = update.getCallbackQuery().getData();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();

            User user = userService.findByTelegramId(chatId);
            if (null != user) {
                if (callData.equals("notice_on") && !user.isNotificationEnabled()) {
                    user.setNotificationEnabled(true);
                    userService.saveOrUpdate(user);
                    sendNotificationSettings(chatId, user, messageId);
                } else if (callData.equals("notice_off") && user.isNotificationEnabled()) {
                    user.setNotificationEnabled(false);
                    userService.saveOrUpdate(user);
                    sendNotificationSettings(chatId, user, messageId);
                }
            }
        }
    }

    private void sendNotificationSettings(Long chatId, User user, Integer msgId) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<List<InlineKeyboardButton>>();
        markupInline.setKeyboard(rowsInline);
        List<InlineKeyboardButton> rowInline = new ArrayList<InlineKeyboardButton>();
        rowsInline.add(rowInline);
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        rowInline.add(inlineKeyboardButton);

        boolean switchOn = user.isNotificationEnabled();
        inlineKeyboardButton.setText(switchOn ? "отключить уведомления" : "включить уведомления");
        inlineKeyboardButton.setCallbackData("notice_" + (switchOn ? "off" : "on"));

        homeGroupBotFacade.send(chatId, msgId, switchOn ? "уведомления включены" : "уведомления отключены", markupInline);
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

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
