package model.homegroups.chain;

import model.homegroups.db.User;
import model.homegroups.facade.BotFacade;
import model.homegroups.facade.DBFacade;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;
import java.util.regex.Pattern;

public class ShowAlertSettingsChain extends Chain {
    private final Pattern pattern = Pattern.compile("\\s*Настройка\\s\\s*уведомлений\\s*", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.CANON_EQ);

    @Override
    public boolean check(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        return pattern.matcher(message.getText()).matches();
    }

    @Override
    public void doJob(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        User user = (User) atr.get(USER_FIELD);
        boolean notificationEnabled = user.isNotificationEnabled();
        String msg = notificationEnabled ? "уведомления включены" : "уведомления отключены";
        botFacade.sendMsg(message.getChatId(), msg, buildInlineAlertKeyboard(notificationEnabled));
    }
}
