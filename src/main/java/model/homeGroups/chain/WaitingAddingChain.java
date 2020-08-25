package model.homeGroups.chain;

import model.homeGroups.facade.BotFacade;
import model.homeGroups.facade.DBFacade;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import utils.Utils;

import java.util.Map;

public class WaitingAddingChain extends Chain {
    @Override
    public boolean check(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        return Utils.isField(atr.get(USER_FIELD)) && Utils.isEmpty(atr.get(HOME_GROUP_FIELD));
    }

    @Override
    public void doJob(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        Long chatId = message.getChatId();
        botFacade.sendMsg(chatId, "Заявка на добавление была внесена, ожидайте принятия.");
    }
}
