package model.homeGroups.chain;

import model.homeGroups.db.StatInfo;
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

public class EnteredStatInfoChain extends Chain {
    private final Pattern pattern = Pattern.compile("\\s*Введено\\s\\s*статистики\\s*", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean check(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        return pattern.matcher(message.getText()).matches();
    }

    @Override
    public void doJob(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        User user = (User) atr.get(USER_FIELD);
        List<StatInfo> allStatInfos = dbFacade.getStatInfoService().findAllByHomeGroupId(user.getHomeGroup().getId());
        botFacade.sendMessageByBlocks(message.getChatId(), buildBlocks(allStatInfos), buildKeyboard(user));
    }

    private List<String> buildBlocks(List<StatInfo> allStatInfos) {
        List<String> result = new ArrayList<>();
        if (Utils.isEmpty(allStatInfos)) {
            result.add("Ничего не было введено");
            return result;
        }

        int i = 1;
        result.add("Введено:\n\n");
        for (StatInfo statInfo : allStatInfos) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(i++);
            stringBuilder.append(") ");
            stringBuilder.append(statInfo.getCount());
            stringBuilder.append(" за ");
            stringBuilder.append(Utils.getStringOfDate(statInfo.getEventDate()));
            stringBuilder.append("\n\n");
            result.add(stringBuilder.toString());
        }
        return result;
    }
}
