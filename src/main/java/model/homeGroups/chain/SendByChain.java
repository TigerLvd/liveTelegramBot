package model.homeGroups.chain;

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
        User user = dbFacade.getUserService().findById(id);
        if (Utils.isEmpty(user)) {
            botFacade.sendMsg(message.getChatId(), "По указанному id пользователь не найден", buildKeyboard((User) atr.get(USER_FIELD)));
            return;
        }

        int count = Integer.parseInt(strings[6]);
        Date date = Utils.getDate(strings[3], strings[4], strings[5]);

        StatInfo statInfo = dbFacade.getStatInfoService().addNewOrUpdate(message.getChatId(), user, date, count);
        botFacade.sendMsg(message.getChatId(), buildMessage(user, statInfo.getCount(), statInfo.getEventDate()), buildKeyboard((User) atr.get(USER_FIELD)));
    }

    private String buildMessage(User user, Integer count, Date date) {
        return "Внесены данные по пользователю " + user.getComment() + " за " + Utils.getStringOfDate(date) + " " + count + " человек.";
    }
}
