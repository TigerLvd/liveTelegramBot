package model.homeGroups.chain;

import model.homeGroups.db.User;
import model.homeGroups.facade.BotFacade;
import model.homeGroups.facade.DBFacade;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class EmptyStatInfoChain extends Chain {
    private final Pattern pattern = Pattern.compile("\\s*Не\\s\\s*заполнено\\s*", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean check(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        return pattern.matcher(message.getText()).matches();
    }

    @Override
    public void doJob(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        User user = (User) atr.get(USER_FIELD);
        botFacade.sendMessageByBlocks(
                message.getChatId(),
                buildMessage(dbFacade, botFacade, user),
                buildKeyboardForUser((User) atr.get(USER_FIELD)));
    }

    private List<String> buildMessage(DBFacade dbFacade, BotFacade botFacade, User user) {
        List<String> result = new ArrayList<>();
        if (!user.hasHomeGroup()) {
            result.add("Нет ячейки");
            return result;
        }

        List<Date> lostWeeks = botFacade.getLostWeeks(dbFacade, user.getHomeGroup());
        if (Utils.isField(lostWeeks)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Нет информации за ");
            stringBuilder.append(lostWeeks.size());
            stringBuilder.append(" недели:\n");
            result.add(stringBuilder.toString());

            int i = 1;
            for (Date monday : lostWeeks) {
                StringBuilder builder = new StringBuilder();
                builder.append(i++);
                builder.append(") ");
                builder.append(Utils.getMndToSunString(monday));
                builder.append("\n");
                result.add(builder.toString());
            }
        } else {
            result.add("Всё заполнено.");
        }
        return result;
    }
}
