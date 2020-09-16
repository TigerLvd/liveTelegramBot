package model.homeGroups.chain;

import model.homeGroups.AlertState;
import model.homeGroups.db.User;
import model.homeGroups.facade.BotFacade;
import model.homeGroups.facade.DBFacade;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import utils.Utils;

import java.util.Map;

public class ChangeAlertSettingsChain extends Chain {

    @Override
    public boolean check(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        return AlertState.getAllCodes().contains(callbackQuery.getData());
    }

    @Override
    public void doJob(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        User user = (User) atr.get(USER_FIELD);
        if (Utils.isField(user)) {
            Long chatId = callbackQuery.getMessage().getChatId();
            Integer messageId = callbackQuery.getMessage().getMessageId();
            String callData = callbackQuery.getData();

            if (callData.equals("notice_on") && !user.isNotificationEnabled()) {
                user.setNotificationEnabled(true);
                dbFacade.getUserService().saveOrUpdate(user);
            } else if (callData.equals("notice_off") && user.isNotificationEnabled()) {
                user.setNotificationEnabled(false);
                dbFacade.getUserService().saveOrUpdate(user);
            }
            boolean notificationEnabled = user.isNotificationEnabled();

            String msg = notificationEnabled ? "уведомления включены" : "уведомления отключены";
            botFacade.updateMsg(chatId, msg, messageId, buildInlineAlertKeyboard(notificationEnabled));
        }
    }
}
