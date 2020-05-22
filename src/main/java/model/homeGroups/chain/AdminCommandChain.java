package model.homeGroups.chain;

import model.homeGroups.facade.BotFacade;
import model.homeGroups.facade.DBFacade;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import utils.Utils;

import java.util.Map;

public class AdminCommandChain extends Chain {
    static private Chain chain;

    static {
//        chain = new AddAdminChain();
//        chain.add(new AddChatChain())
//                .add(new AddPhoneChain())
//                .add(new AddNameChain())
//                .add(new StartChain())
//                .add(new PauseChain())
//                .add(new StopChain())
//                .add(new ShowStatusListChain())
//                .add(new ShowStatusChain())
//                .add(new ChooseTimeChain())
//                .add(new InfoChain())
//                .add(new ChangeLessonChain())
//                .add(new SendLessonChain())
//                .add(new SetWithSupportChain())
//                .add(new SetWithOutSupportChain())
//                .add(new ErrorInputChain());
    }

    public String getCommand() {
        return null;
    }

    @Override
    public boolean check(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        return Utils.isField(message) && message.hasText() && message.getText().contains("/");
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
