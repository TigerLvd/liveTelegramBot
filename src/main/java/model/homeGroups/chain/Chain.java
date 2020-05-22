package model.homeGroups.chain;

import model.homeGroups.facade.BotFacade;
import model.homeGroups.facade.DBFacade;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;

public abstract class Chain {
    public static final String USER_FIELD = "user";

    private Chain next = null;

    public Chain getNext() {
        return next;
    }

    private void setNext(Chain next) {
        this.next = next;
    }

    public boolean hasNext() {
        return getNext() != null;
    }

    public Chain add(Chain chain) {
        setNext(chain);
        return getNext();
    }

    public abstract boolean check(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr);

    public abstract void doJob(final DBFacade dbFacade, BotFacade botFacade, final Message message, CallbackQuery callbackQuery, Map<String, Object> atr);

    public abstract String getCommand();

    public String getExtCommand(BotFacade botFacade) {
        return getCommand() + "@" + botFacade.getBotShortName();
    }

//    public abstract Pattern getPattern();

    public String getArg(Message message, BotFacade botFacade) {
        String text = message.getText();
        if (text.startsWith(getExtCommand(botFacade) + " ")) {
            return text.split(getExtCommand(botFacade) + " ")[1];
        }
        return text.split(getCommand() + " ")[1];
    }

    public boolean canGetArg(Message message, BotFacade botFacade) {
        String text = message.getText();
        if (text.startsWith(getExtCommand(botFacade))) {
            String[] split = text.split(getExtCommand(botFacade) + " ");
            return split.length >= 2;
        }
        String[] split = text.split(getCommand() + " ");
        return split.length >= 2;
    }
}
