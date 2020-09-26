package model.homeGroups.chain;

import model.homeGroups.db.HomeGroup;
import model.homeGroups.db.StatInfo;
import model.homeGroups.db.User;
import model.homeGroups.facade.BotFacade;
import model.homeGroups.facade.DBFacade;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import utils.Utils;

import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

public class SendByChain extends Chain {
    private final Pattern pattern = Pattern.compile("\\s*Ввод\\s\\s*за\\s\\s*(\\d|\\d{2})\\s\\s*(\\d|\\d{2})\\.(\\d|\\d{2})\\.(\\d{4}|\\d{2})\\s\\s*(\\d|\\d{2})\\s*", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean check(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        return pattern.matcher(message.getText()).matches() && isFieldAndTrue(atr.get(IS_ROOT_FIELD));
    }

    @Override
    public void doJob(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        String text = message.getText().trim();
        String[] strings = text.trim().split("\\s\\s*");
        Long id = Long.parseLong(strings[2].trim());
        HomeGroup homeGroup = dbFacade.getHomeGroupService().findById(id);
        User user = (User) atr.get(USER_FIELD);
        if (Utils.isEmpty(homeGroup)) {
            botFacade.sendMsg(message.getChatId(), "По указанному id ячейка не найдена", buildKeyboardForUser(user));
            return;
        }

        int count = Integer.parseInt(strings[4]);
        String[] dateSplit = strings[3].split("\\.");
        Date date = Utils.getDate(dateSplit[0], dateSplit[1], dateSplit[2]);

        StatInfo statInfo = dbFacade.getStatInfoService().addNewOrUpdate(message.getChatId(), homeGroup, user, date, count);
        botFacade.sendMsg(message.getChatId(), buildMessage(homeGroup, statInfo.getCount(), statInfo.getEventDate()), buildKeyboardForUser(user));
    }

    private String buildMessage(HomeGroup homeGroup, Integer count, Date date) {
        return "Внесены данные по ячейке " + homeGroup.getComment() + " за " + Utils.getStringOfDate(date) + " " + count + " человек.";
    }
}
