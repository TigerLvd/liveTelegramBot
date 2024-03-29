package model.homegroups.chain;

import model.homegroups.db.User;
import model.homegroups.facade.BotFacade;
import model.homegroups.facade.DBFacade;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import utils.Utils;

import java.util.Map;

public class CallBackChain extends Chain {
    private static Chain chain;

    static {
        chain = new ChangeAlertSettingsChain();
        chain.add(new InfoAboutCallbackChain());
    }

    @Override
    public boolean check(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        if (Utils.isField(callbackQuery)) {
            Long chatId = callbackQuery.getMessage().getChatId();
            User user = dbFacade.getUserService().findByTelegramId(chatId);
            atr.put(USER_FIELD, user);
        }
        return Utils.isField(callbackQuery);
    }

    @Override
    public void doJob(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        goByChain(chain, dbFacade, botFacade, message, callbackQuery, atr);
    }
}
