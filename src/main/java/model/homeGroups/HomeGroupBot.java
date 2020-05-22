package model.homeGroups;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import model.homeGroups.chain.ShowAlertSettingsChain;
import model.homeGroups.chain.Chain;
import model.homeGroups.chain.DownloadStatInfosChain;
import model.homeGroups.chain.EmptyStatInfoDaysChain;
import model.homeGroups.chain.EmptyUsersStatInfoDaysChain;
import model.homeGroups.chain.EntedStatInfoChain;
import model.homeGroups.chain.ExampleInfoAboutChain;
import model.homeGroups.chain.ExampleInputStatInfoChain;
import model.homeGroups.chain.HomeGroupListChain;
import model.homeGroups.chain.InfoAboutChain;
import model.homeGroups.chain.InputStatInfoChain;
import model.homeGroups.chain.NewUsersChain;
import model.homeGroups.chain.SendByChain;
import model.homeGroups.chain.UsersChain;
import model.homeGroups.db.User;
import model.homeGroups.service.UserService;

public class HomeGroupBot extends TelegramLongPollingBot {

    private final Long adminId;
    private final String botToken;
    private final String botName;

    private HomeGroupBotFacade homeGroupBotFacade;
    private UserService userService;

    private final static String TO_INPUT_STAT_INFO_FIELD = "Ввод статистики";
    private final static String INPUT_STAT_INFO_BY_FIELD = "Ввод за";
    private final static String ENTERED_STAT_INFO_FIELD = "Введено статистики";
    private final static String USER_FIELD = "Пользователи";
    private final static String EMPTY_STAT_INFO_FIELD = "Не заполнено";
    private final static String ALERT_SETTINGS_FIELD = "Настройка уведомлений";
    private final static String HOME_GROUPS_LIST_FIELD = "Список ячеек";
    private final static String NEW_USERS_LIST_FIELD = "Новые пользователи";
    private final static String INFO_ABOUT_FIELD = "Инфа по ";
    private final static String INFO_ABOUT_FIELD2 = "Инфа по";
    private final static String EMPTY_USERS_STAT_INFO_FIELD = "Не заполнено у пользователей";
    private final static String DOWNLOAD_STAT_INFOS = "Скачать статистику в xsl";

    private Chain chain;

    public void fillChains() {
        chain = new ShowAlertSettingsChain();
        chain.add(new DownloadStatInfosChain())
                .add(new EmptyStatInfoDaysChain())
                .add(new EmptyUsersStatInfoDaysChain())
                .add(new EntedStatInfoChain())
                .add(new ExampleInfoAboutChain())
                .add(new ExampleInputStatInfoChain())
                .add(new HomeGroupListChain())
                .add(new InfoAboutChain())
                .add(new InputStatInfoChain())
                .add(new NewUsersChain())
                .add(new SendByChain())
                .add(new UsersChain());
    }

    public HomeGroupBot(String botToken, String botName, Long adminId, DefaultBotOptions options, UserService userService,
                        HomeGroupBotFacade homeGroupBotFacade) {
        super(options);

        this.botToken = botToken;
        this.botName = botName;
        this.adminId = adminId;

        this.userService = userService;
        this.homeGroupBotFacade = homeGroupBotFacade;
        this.homeGroupBotFacade.setBot(this);

        fillChains();
    }

    public HomeGroupBot(String botToken, String botName, Long adminId, UserService userService, HomeGroupBotFacade homeGroupBotFacade) {
        super();

        this.botToken = botToken;
        this.botName = botName;
        this.adminId = adminId;

        this.userService = userService;
        this.homeGroupBotFacade = homeGroupBotFacade;
        this.homeGroupBotFacade.setBot(this);

        fillChains();
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
                } else if ("alskd5".equals(text)) {
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

            if (!user.hasHomeGroup() && !user.isAdmin()) {
                // когда ввёл пароль, но пользователя ещё не добавили в БД:
                homeGroupBotFacade.send(chatId, "Пользователь был добавлен, информация обрабатывается, функционал пока не доступен.");
                return;
            }

            ReplyKeyboardMarkup keyboardMarkup;

            if (user.isAdmin()) {
                if (user.hasHomeGroup()) {
                    keyboardMarkup = new CustomKeyboardMarkup(USER_FIELD, TO_INPUT_STAT_INFO_FIELD, EMPTY_STAT_INFO_FIELD, ENTERED_STAT_INFO_FIELD, ALERT_SETTINGS_FIELD, HOME_GROUPS_LIST_FIELD, NEW_USERS_LIST_FIELD, INFO_ABOUT_FIELD, EMPTY_USERS_STAT_INFO_FIELD, DOWNLOAD_STAT_INFOS, INPUT_STAT_INFO_BY_FIELD);
                } else {
                    keyboardMarkup = new CustomKeyboardMarkup(USER_FIELD, HOME_GROUPS_LIST_FIELD, NEW_USERS_LIST_FIELD, INFO_ABOUT_FIELD, EMPTY_USERS_STAT_INFO_FIELD, DOWNLOAD_STAT_INFOS, INPUT_STAT_INFO_BY_FIELD);
                }
            } else {
                keyboardMarkup = new CustomKeyboardMarkup(TO_INPUT_STAT_INFO_FIELD, EMPTY_STAT_INFO_FIELD, ENTERED_STAT_INFO_FIELD, ALERT_SETTINGS_FIELD, HOME_GROUPS_LIST_FIELD);
            }
            Chain currentChain = chain;
//            while (!currentChain.check(text, user.hasHomeGroup(), user.isAdmin()) && currentChain.hasNext()) {
//                currentChain = currentChain.getNext();
//            }
//            if (currentChain.check(text, user.hasHomeGroup(), user.isAdmin())) {
//                currentChain.doJob(homeGroupBotFacade, chatId, text, user, keyboardMarkup, adminId);
//                return;
//            }

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

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
