package model.homeGroups.chain;

import model.homeGroups.db.User;
import model.homeGroups.facade.BotFacade;
import model.homeGroups.facade.DBFacade;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ShowUsersChain extends Chain {
    private final Pattern pattern = Pattern.compile("\\s*Пользователи\\s*", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean check(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        return pattern.matcher(message.getText()).matches();
    }

    @Override
    public void doJob(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        User user = (User) atr.get(USER_FIELD);
        List<User> allUsers = dbFacade.getUserService().findAll();
        botFacade.sendMessageByBlocks(user.getId(), buildBlocks(allUsers), buildKeyboard(user));
    }

    private List<String> buildBlocks(List<User> allUsers) {
        List<String> result = new ArrayList<>();
        if (Utils.isEmpty(allUsers)) {
            result.add("Пусто");
            return result;
        }
        int i = 1;
        for (User usr : allUsers) {
            StringBuilder userInfos = new StringBuilder();
            userInfos.append(i++);
            userInfos.append(") ");
            userInfos.append("admin=");
            userInfos.append(usr.isAdmin());
            userInfos.append(", ");
            userInfos.append("id=");
            userInfos.append(usr.getId());
            userInfos.append(", chatId=");
            userInfos.append(usr.getTelegramUserId());
            userInfos.append(", firstName=");
            userInfos.append(usr.getFirstName());
            userInfos.append(", lastName=");
            userInfos.append(usr.getLastName());
            userInfos.append(", nickName=");
            userInfos.append(usr.getNickName());
            userInfos.append(", comment=");
            userInfos.append(usr.getComment());
            userInfos.append(", isLeader=");
            userInfos.append(usr.isLeader());
            userInfos.append("\n\n");

            result.add(userInfos.toString());
        }
        return result;
    }
}
