package model.homegroups.chain;

import model.homegroups.db.StatInfo;
import model.homegroups.db.User;
import model.homegroups.facade.BotFacade;
import model.homegroups.facade.DBFacade;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import utils.Utils;

import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

public class InputStatInfoChain extends Chain {
    private final Pattern pattern = Pattern.compile("\\s*(Статистика:|Статистика)\\s\\s*(\\d|\\d{2})\\.(\\d|\\d{2})\\.(\\d{4}|\\d{2})\\s\\s*\\d\\d*\\s*", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    @Override
    public boolean check(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        return pattern.matcher(message.getText()).matches();
    }

    @Override
    public void doJob(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        String[] str;
        Date date;
        User user = (User) atr.get(USER_FIELD);
        try {
            str = message.getText().trim().split("\\s\\s*");
            String[] sbStr = str[1].split("\\.");

            date = Utils.getDate(sbStr[0], sbStr[1], sbStr[2]);
        } catch (Exception e) {
            sendExample(botFacade, message, user);
            return;
        }
        int count;
        try {
            count = Integer.parseInt(str[2]);
        } catch (Exception e) {
            sendExample(botFacade, message, user);
            return;
        }

        StatInfo statInfo = dbFacade.getStatInfoService().addNewOrUpdate(message.getChatId(), user.getHomeGroup(), user, date, count);

        botFacade.sendMsg(message.getChatId(), "Получено: " + statInfo.getCount() + " за " + Utils.getStringOfDate(statInfo.getEventDate()), buildKeyboardForUser(user));
    }

    private void sendExample(BotFacade botFacade, Message message, User user) {
        String msg = "Не верный формат! Ввести информацию в формате: Статистика: ДД.ММ.ГГ количество. Например:";
        String msg2 = "Статистика: " + Utils.getStringOfDate(new Date()) + " 5";
        botFacade.sendMsg(message.getChatId(), msg, buildKeyboardForUser(user));
        botFacade.sendMsg(message.getChatId(), msg2, buildKeyboardForUser(user));
    }
}
