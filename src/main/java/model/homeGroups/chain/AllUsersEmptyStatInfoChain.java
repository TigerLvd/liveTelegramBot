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

public class AllUsersEmptyStatInfoChain extends Chain {
    private final Pattern pattern = Pattern.compile("\\s*Не\\s\\s*заполнено\\s\\s*у\\s\\s*пользователей\\s*", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean check(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        return pattern.matcher(message.getText()).matches();
    }

    @Override
    public void doJob(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        List<User> users = dbFacade.getUserService().findAll();
        botFacade.sendMessageByBlocks(message.getChatId(), buildMessage(dbFacade, botFacade, users), buildKeyboardForUser((User) atr.get(USER_FIELD)));
    }

    private List<String> buildMessage(DBFacade dbFacade, BotFacade botFacade, List<User> users) {
        List<String> result = new ArrayList<>();
        if (Utils.isEmpty(users)) {
            result.add("Нет пользователей");
            return result;
        }
        if (users.stream().noneMatch(User::hasHomeGroup)) {
            result.add("Нет пользователей с домашними группами");
            return result;
        }

        for (User user : users) {
            if (user.hasHomeGroup()) {
                List<Date> lostWeeks = botFacade.getLostWeeks(dbFacade, user.getHomeGroup());
                if (!lostWeeks.isEmpty()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("У пользователя ");
                    stringBuilder.append(user.getComment());
                    stringBuilder.append(" нет информации за следующие недели:\n");
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
                }
            }
        }
        return result;
    }
}
