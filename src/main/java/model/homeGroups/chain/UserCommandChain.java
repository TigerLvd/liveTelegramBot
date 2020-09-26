package model.homeGroups.chain;

import model.homeGroups.facade.BotFacade;
import model.homeGroups.facade.DBFacade;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import utils.Utils;

import java.util.Map;

public class UserCommandChain extends Chain {
    static private Chain chain;

    static {
        chain = new InputStatInfoChain();
        chain.add(new ExampleInputStatInfoChain())
                .add(new ShowHomeGroupListChain())
                .add(new EmptyStatInfoChain())
                .add(new EnteredStatInfoChain())
                .add(new ShowAlertSettingsChain());
    }

    @Override
    public boolean check(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        return isCommandTextMessage(message) ;
    }

    @Override
    public void doJob(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        goByChain(chain, dbFacade, botFacade, message, callbackQuery, atr);
    }
}
