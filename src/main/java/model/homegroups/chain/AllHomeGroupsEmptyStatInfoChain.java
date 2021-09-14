package model.homegroups.chain;

import model.homegroups.db.HomeGroup;
import model.homegroups.db.User;
import model.homegroups.facade.BotFacade;
import model.homegroups.facade.DBFacade;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class AllHomeGroupsEmptyStatInfoChain extends Chain {
    private final Pattern pattern = Pattern.compile("\\s*Не\\s\\s*заполнено\\s\\s*у\\s\\s*ячеек\\s*", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    @Override
    public boolean check(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        return pattern.matcher(message.getText()).matches();
    }

    @Override
    public void doJob(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        List<HomeGroup> homeGroups = dbFacade.getHomeGroupService().findAll();
        botFacade.sendMessageByBlocks(message.getChatId(), buildMessage(dbFacade, botFacade, homeGroups), buildKeyboardForUser((User) atr.get(USER_FIELD)));
    }

    private List<String> buildMessage(DBFacade dbFacade, BotFacade botFacade, List<HomeGroup> homeGroups) {
        List<String> result = new ArrayList<>();
        if (Utils.isEmpty(homeGroups)) {
            result.add("Нет ячеек");
            return result;
        }

        for (HomeGroup homeGroup : homeGroups) {
            List<Date> lostWeeks = botFacade.getLostWeeks(dbFacade, homeGroup);
            if (Utils.isField(lostWeeks)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("\nУ ячейки ");
                stringBuilder.append(homeGroup.getComment());
                stringBuilder.append(" нет информации за ");
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
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("\nУ ячейки ");
                stringBuilder.append(homeGroup.getComment());
                stringBuilder.append(" всё введено.\n");
                result.add(stringBuilder.toString());
            }
        }
        return result;
    }
}
