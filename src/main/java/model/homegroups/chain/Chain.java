package model.homegroups.chain;

import model.homegroups.CustomKeyboardMarkup;
import model.homegroups.db.HasComment;
import model.homegroups.db.HasId;
import model.homegroups.db.User;
import model.homegroups.facade.BotFacade;
import model.homegroups.facade.DBFacade;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Chain {
    protected static final String USER_FIELD = "user";
    protected static final String HOME_GROUP_FIELD = "home_group";
    protected static final String IS_ADMIN_FIELD = "is_admin";
    protected static final String IS_ROOT_FIELD = "is_root";
    protected static final String TO_INPUT_STAT_INFO_COMMAND = "Ввод статистики";
    protected static final String INPUT_STAT_INFO_BY_COMMAND = "Ввод за";
    protected static final String ENTERED_STAT_INFO_COMMAND = "Введено статистики";
    protected static final String USER_COMMAND = "Пользователи";
    protected static final String EMPTY_STAT_INFO_COMMAND = "Не заполнено";
    protected static final String ALERT_SETTINGS_COMMAND = "Настройка уведомлений";
    protected static final String HOME_GROUPS_LIST_COMMAND = "Список ячеек";
    protected static final String NEW_USERS_LIST_COMMAND = "Новые пользователи";
    protected static final String INFO_ABOUT_COMMAND = "Инфа по ";
    protected static final String EMPTY_HOME_GROUPS_STAT_INFO_COMMAND = "Не заполнено у ячеек";
    protected static final String DOWNLOAD_STAT_INFOS = "Скачать статистику в xsl";

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
        List<String> labels = new ArrayList<>();
        if (user.hasHomeGroup()) {
            labels.add(TO_INPUT_STAT_INFO_COMMAND);
            labels.add(EMPTY_STAT_INFO_COMMAND);
            labels.add(ENTERED_STAT_INFO_COMMAND);
            labels.add(HOME_GROUPS_LIST_COMMAND);
            labels.add(ALERT_SETTINGS_COMMAND);
        }
        if (user.isAdmin()) {
            labels.add(INPUT_STAT_INFO_BY_COMMAND);
            labels.add(EMPTY_HOME_GROUPS_STAT_INFO_COMMAND);
            labels.add(USER_COMMAND);
            labels.add(NEW_USERS_LIST_COMMAND);
            labels.add(INFO_ABOUT_COMMAND);
//            labels.add(DOWNLOAD_STAT_INFOS);
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

    protected <K extends HasComment & HasId> InlineKeyboardMarkup buildInlineChooseUserKeyboard(String prefix, List<K> holders) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        int size = holders.size();
        int i = 0;
        while (i < size) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            addRowInline(prefix, holders.get(i++), rowInline);
            if (i < size) {
                addRowInline(prefix, holders.get(i++), rowInline);
            }

            rowsInline.add(rowInline);
        }
        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }

    protected <K extends HasComment & HasId> void addRowInline(String prefix, K holder, List<InlineKeyboardButton> rowInline) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(holder.getComment());
        inlineKeyboardButton.setCallbackData(prefix + holder.getId());
        rowInline.add(inlineKeyboardButton);
    }
}
