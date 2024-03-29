package model.homegroups.chain;

import model.homegroups.facade.BotFacade;
import model.homegroups.facade.DBFacade;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;

public class AdminChain extends Chain {
    private static Chain chain;

    static {
        chain = new ShowUsersChain();
        chain.add(new ShowHomeGroupListChain())
                .add(new ShowNewUsersChain())
                .add(new InfoAboutChain())
                .add(new ExampleInfoAboutChain())
                .add(new AllHomeGroupsEmptyStatInfoChain())
//                .add(new DownloadStatInfosChain())
                .add(new SendByChain())
                .add(new ExampleSendByChain())
                .add(new InputStatInfoChain())
                .add(new ExampleInputStatInfoChain())
                .add(new EmptyStatInfoChain())
                .add(new EnteredStatInfoChain())
                .add(new ShowAlertSettingsChain())
                .add(new InvalidInputChain());
    }

    @Override
    public boolean check(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        return isCommandTextMessage(message) && isFieldAndTrue(atr.get(IS_ADMIN_FIELD));
    }

    @Override
    public void doJob(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        goByChain(chain, dbFacade, botFacade, message, callbackQuery, atr);
    }
}
