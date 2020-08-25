package model.homeGroups.chain;

import model.homeGroups.facade.BotFacade;
import model.homeGroups.facade.DBFacade;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import utils.Utils;

import java.util.Map;

public class AuthorityChain extends Chain {
    private final Chain chain;

    public AuthorityChain(Long adminId) {
        chain = new AddUserChain(adminId);
        chain.add(new WaitingAddingChain());
    }

    @Override
    public boolean check(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        return Utils.isEmpty(atr.get(HOME_GROUP_FIELD));
    }

    @Override
    public void doJob(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        goByChain(chain, dbFacade, botFacade, message, callbackQuery, atr);
    }
}
