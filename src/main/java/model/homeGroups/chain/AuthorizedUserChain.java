package model.homeGroups.chain;

import model.homeGroups.db.User;
import model.homeGroups.facade.BotFacade;
import model.homeGroups.facade.DBFacade;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import utils.Utils;

import java.util.Map;

public class AuthorizedUserChain extends Chain {
    private final Chain chain;
    private Long rootId;

    public AuthorizedUserChain(Long rootId) {
        this.rootId = rootId;
        chain = new CallBackChain();
        chain.add(new AdminChain())
                .add(new UserCommandChain());
    }

    @Override
    public boolean check(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        Long chatId = getChatId(message, callbackQuery);
        if (Utils.isEmpty(chatId)) {
            return false;
        }
        User user = dbFacade.getUserService().findByTelegramId(chatId);
        if (Utils.isField(user)) {
            atr.put(USER_FIELD, user);
            atr.put(IS_ADMIN_FIELD, Boolean.TRUE.equals(user.isAdmin()));
            atr.put(IS_ROOT_FIELD, user.getTelegramUserId().equals(rootId));
            if (user.hasHomeGroup()) {
                atr.put(HOME_GROUP_FIELD, user.getHomeGroup());
            }
            return true;
        }
        return false;
    }

    private Long getChatId(Message message, CallbackQuery callbackQuery) {
        if (Utils.isField(callbackQuery) && Utils.isField(callbackQuery.getMessage())) {
            return callbackQuery.getMessage().getChatId();
        } else if (Utils.isField(message)) {
            return message.getChatId();
        }
        return null;
    }

    @Override
    public void doJob(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        goByChain(chain, dbFacade, botFacade, message, callbackQuery, atr);
    }
}
