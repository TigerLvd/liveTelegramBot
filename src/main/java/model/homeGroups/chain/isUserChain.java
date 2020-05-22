package model.homeGroups.chain;

import model.homeGroups.db.User;
import model.homeGroups.facade.BotFacade;
import model.homeGroups.facade.DBFacade;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import utils.Utils;

import java.util.Map;

public class isUserChain extends Chain {
    static private Chain chain;

    static {
        chain = new CallBackChain();
        chain.add(new AdminCommandChain())
                .add(new UserCommandChain());
    }

    public String getCommand() {
        return null;
    }

    @Override
    public boolean check(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        Long chatId = null;
        if (Utils.isField(callbackQuery) && Utils.isField(callbackQuery.getMessage())) {
            chatId = callbackQuery.getMessage().getChatId();
        } else if (Utils.isField(message)) {
            chatId = message.getChatId();
        }
        if (Utils.isEmpty(chatId)) {
            return false;
        }
        User user = dbFacade.getUserService().findByTelegramId(chatId);
        if (Utils.isField(user)) {
            atr.put(USER_FIELD, user);
            return true;
        }
        return false;
    }

    @Override
    public void doJob(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        Chain currentChain = chain;
        while (null != currentChain && !currentChain.check(dbFacade, botFacade, message, callbackQuery, atr)) {
            currentChain = currentChain.getNext();
        }
        if (null != currentChain && currentChain.check(dbFacade, botFacade, message, callbackQuery, atr)) {
            currentChain.doJob(dbFacade, botFacade, message, callbackQuery, atr);
        }
    }
}
