package model.homeGroups.chain;

import model.homeGroups.db.User;
import model.homeGroups.facade.BotFacade;
import model.homeGroups.facade.DBFacade;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import utils.Utils;

import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

public class ExampleInputStatInfoChain extends Chain {
    private final Pattern pattern = Pattern.compile("\\s*Ввод\\s\\s*статистики\\s*", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean check(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        return pattern.matcher(message.getText()).matches();
    }

    @Override
    public void doJob(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        String msg = "Введите информацию в формате: Статистика: дд.мм.гг количество.";
        String msg2 = "Обратите внимание, что слово \"<b>Статистика</b>\" должна быть в отправляемом сообщении!\n\n" +
                "Для удобства можно скопировать и изменить пример:";
        String msg3 = "Статистика: " + Utils.getStringOfDate(new Date()) + " 5";

        User user = (User) atr.get(USER_FIELD);

        botFacade.sendMsg(message.getChatId(), msg, buildKeyboard(user));
        botFacade.sendMsg(message.getChatId(), msg2, buildKeyboard(user));
        botFacade.sendMsg(message.getChatId(), msg3, buildKeyboard(user));
    }
}
