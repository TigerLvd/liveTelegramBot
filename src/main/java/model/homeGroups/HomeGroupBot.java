package model.homeGroups;

import model.homeGroups.chain.*;
import model.homeGroups.facade.BotFacade;
import model.homeGroups.facade.DBFacade;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;

public class HomeGroupBot extends TelegramLongPollingBot {

    private final Long adminId;
    private final String botToken;
    private final String botName;

    private BotFacade botFacade;
    private DBFacade dbFacade;
    private Chain chain;

    public void fillChains() {
        chain = new StartChain();
        chain.add(new AuthorizedUserChain(adminId))
                .add(new AuthorityChain(adminId))
                .add(new NotAuthorizedErrorChain());
    }

    public HomeGroupBot(String botToken, String botName, Long adminId, DefaultBotOptions options, BotFacade botFacade, DBFacade dbFacade) {
        super(options);

        this.botToken = botToken;
        this.botName = botName;
        this.adminId = adminId;

        this.botFacade = botFacade;
        this.botFacade.init(this, botName);
        this.dbFacade = dbFacade;

        fillChains();
    }

    public HomeGroupBot(String botToken, String botName, Long adminId, BotFacade botFacade, DBFacade dbFacade) {
        super();

        this.botToken = botToken;
        this.botName = botName;
        this.adminId = adminId;

        this.botFacade = botFacade;
        this.dbFacade = dbFacade;

        fillChains();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText() || update.hasCallbackQuery()) {
            Chain currentChain = chain;
            HashMap<String, Object> atr = new HashMap<>();
            while (null != currentChain && !currentChain.check(dbFacade, botFacade, update.getMessage(), update.getCallbackQuery(), atr)) {
                currentChain = currentChain.getNext();
            }
            if (null != currentChain && currentChain.check(dbFacade, botFacade, update.getMessage(), update.getCallbackQuery(), atr)) {
                currentChain.doJob(dbFacade, botFacade, update.getMessage(), update.getCallbackQuery(), atr);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
