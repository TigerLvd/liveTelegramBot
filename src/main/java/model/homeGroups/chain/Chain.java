package model.homeGroups.chain;

import model.homeGroups.CustomKeyboardMarkup;
import model.homeGroups.db.User;
import model.homeGroups.facade.BotFacade;
import model.homeGroups.facade.DBFacade;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import utils.Utils;

import java.util.*;

public abstract class Chain {
    protected static final String USER_FIELD = "user";
    protected static final String HOME_GROUP_FIELD = "home_group";
    protected static final String IS_ADMIN_FIELD = "is_admin";
    protected static final String IS_ROOT_FIELD = "is_root";
    protected final static String TO_INPUT_STAT_INFO_COMMAND = "Ввод статистики";
    protected final static String INPUT_STAT_INFO_BY_COMMAND = "Ввод за";
    protected final static String ENTERED_STAT_INFO_COMMAND = "Введено статистики";
    protected final static String USER_COMMAND = "Пользователи";
    protected final static String EMPTY_STAT_INFO_COMMAND = "Не заполнено";
    protected final static String ALERT_SETTINGS_COMMAND = "Настройка уведомлений";
    protected final static String HOME_GROUPS_LIST_COMMAND = "Список ячеек";
    protected final static String NEW_USERS_LIST_COMMAND = "Новые пользователи";
    protected final static String INFO_ABOUT_COMMAND = "Инфа по ";
    protected final static String INFO_ABOUT_COMMAND2 = "Инфа по";
    protected final static String EMPTY_USERS_STAT_INFO_COMMAND = "Не заполнено у пользователей";
    protected final static String DOWNLOAD_STAT_INFOS = "Скачать статистику в xsl";

    private Chain next = null;

    public Chain getNext() {
        return next;
    }

    private void setNext(Chain next) {
        this.next = next;
    }

    public Chain add(Chain chain) {
        setNext(chain);
        return getNext();
    }

    public abstract boolean check(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr);

    public abstract void doJob(final DBFacade dbFacade, BotFacade botFacade, final Message message, CallbackQuery callbackQuery, Map<String, Object> atr);

    protected void goByChain(final Chain firstChain, DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        Chain currentChain = firstChain;
        while (null != currentChain && !currentChain.check(dbFacade, botFacade, message, callbackQuery, atr)) {
            currentChain = currentChain.getNext();
        }
        if (null != currentChain && currentChain.check(dbFacade, botFacade, message, callbackQuery, atr)) {
            currentChain.doJob(dbFacade, botFacade, message, callbackQuery, atr);
        }
    }

    public String getCommand() {
        return null;
    }

    public static ReplyKeyboardMarkup buildKeyboardForUser(User user) {
        Set<String> labels = new HashSet<>();
        if (user.hasHomeGroup()) {
            labels.add(TO_INPUT_STAT_INFO_COMMAND);
            labels.add(EMPTY_STAT_INFO_COMMAND);
            labels.add(ENTERED_STAT_INFO_COMMAND);
            labels.add(ALERT_SETTINGS_COMMAND);
            labels.add(HOME_GROUPS_LIST_COMMAND);
        }
        if (user.isAdmin()) {
            labels.add(USER_COMMAND);
            labels.add(NEW_USERS_LIST_COMMAND);
            labels.add(INFO_ABOUT_COMMAND);
            labels.add(EMPTY_USERS_STAT_INFO_COMMAND);
            labels.add(DOWNLOAD_STAT_INFOS);
            labels.add(INPUT_STAT_INFO_BY_COMMAND);
        }
        return Utils.isField(labels) ? new CustomKeyboardMarkup(labels) : null;
    }

    protected boolean isCommandTextMessage(Message message) {
        return Utils.isField(message) && message.hasText();
    }

    protected boolean isFieldAndTrue(Object o) {
        return Utils.isField(o) && Boolean.TRUE.equals(o);
    }

    protected InlineKeyboardMarkup buildInlineAlertKeyboard(boolean switchOn) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();

        inlineKeyboardButton.setText(switchOn ? "отключить уведомления" : "включить уведомления");
        inlineKeyboardButton.setCallbackData("notice_" + (switchOn ? "off" : "on"));
        rowInline.add(inlineKeyboardButton);
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }

    protected InlineKeyboardMarkup buildInlineChooseUserKeyboard(String prefix, List<User> users) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        int size = users.size();
        int i = 0;
        while (i < size) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            addRowInline(prefix, users.get(i++), rowInline);
            if (i < size) {
                addRowInline(prefix, users.get(i++), rowInline);
            }

            rowsInline.add(rowInline);
        }
        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }

    protected void addRowInline(String prefix, User user, List<InlineKeyboardButton> rowInline) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(user.getComment());
        inlineKeyboardButton.setCallbackData(prefix + user.getId());
        rowInline.add(inlineKeyboardButton);
    }
}
