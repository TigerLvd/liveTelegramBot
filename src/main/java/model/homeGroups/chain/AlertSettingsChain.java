package model.homeGroups.chain;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import model.homeGroups.HomeGroupBotFacade;
import model.homeGroups.db.User;

public class AlertSettingsChain extends Chain {
    private final Pattern pattern = Pattern.compile("Настройка\\s*уведомлений", Pattern.CASE_INSENSITIVE);

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public void doJob(final HomeGroupBotFacade homeGroupBotFacade, final Long chatId, final String text, final User user, final ReplyKeyboardMarkup keyboardMarkup, final Long adminId) {
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

        homeGroupBotFacade.send(chatId, null, switchOn ? "уведомления включены" : "уведомления отключены", markupInline);
    }

    public boolean check(String value, boolean hasHomeGroup, boolean isAdmin) {
        return hasHomeGroup && getPattern().matcher(value).matches();
    }
}
