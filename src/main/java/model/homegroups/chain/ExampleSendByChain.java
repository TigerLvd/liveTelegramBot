package model.homegroups.chain;

import model.homegroups.db.User;
import model.homegroups.facade.BotFacade;
import model.homegroups.facade.DBFacade;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;
import java.util.regex.Pattern;

public class ExampleSendByChain extends Chain {
    private final Pattern pattern = Pattern.compile("\\s*Ввод\\s\\s*за\\s*", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    @Override
    public boolean check(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        return pattern.matcher(message.getText()).matches() && isFieldAndTrue(atr.get(IS_ROOT_FIELD));
    }

    @Override
    public void doJob(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        String msg = "Введите информацию в формате: Ввод за <№ ячейки> <дд.мм.гг> <количество>";
        botFacade.sendMsg(message.getChatId(), msg, buildKeyboardForUser((User) atr.get(USER_FIELD)));
    }
}
