package model.homeGroups.chain;

import model.homeGroups.db.User;
import model.homeGroups.facade.BotFacade;
import model.homeGroups.facade.DBFacade;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ShowAlertSettingsChain extends Chain {
    private final Pattern pattern = Pattern.compile("\\s*Настройка\\s\\s*уведомлений\\s*", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean check(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        return pattern.matcher(message.getText()).matches();
    }

    @Override
    public void doJob(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        markupInline.setKeyboard(rowsInline);
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowsInline.add(rowInline);
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        rowInline.add(inlineKeyboardButton);

        User user = (User) atr.get(USER_FIELD);
        boolean switchOn = user.isNotificationEnabled();
        inlineKeyboardButton.setText(switchOn ? "отключить уведомления" : "включить уведомления");
        inlineKeyboardButton.setCallbackData("notice_" + (switchOn ? "off" : "on"));

        botFacade.sendMsg(message.getChatId(), switchOn ? "уведомления включены" : "уведомления отключены", markupInline);
    }
}
