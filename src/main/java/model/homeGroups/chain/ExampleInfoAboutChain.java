package model.homeGroups.chain;

import model.homeGroups.db.User;
import model.homeGroups.facade.BotFacade;
import model.homeGroups.facade.DBFacade;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import utils.Utils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ExampleInfoAboutChain extends Chain {
    private final Pattern pattern = Pattern.compile("\\s*Инфа\\s\\s*по\\s*", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean check(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        return pattern.matcher(message.getText()).matches();
    }

    @Override
    public void doJob(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        User user = (User) atr.get(USER_FIELD);
        botFacade.sendMsg(message.getChatId(), "Введите команду в формате: Инфа по "
                + "<id пользователя> (id пользователей можно посмотреть в списке пользователей - кнопка \"Пользователи\"). Например:", buildKeyboardForUser(user));
        botFacade.sendMsg(message.getChatId(), "Инфа по " + user.getId(), buildKeyboardForUser(user));
        List<User> users = dbFacade.getUserService().findAll();
        if (Utils.isField(users)) {
            botFacade.sendMsg(
                    message.getChatId(),
                    "Или выбирете пользователя:",
                    buildInlineChooseUserKeyboard("info_about_",
                            users.stream()
                                    .sorted(Comparator.comparing(User::getComment))
                                    .collect(Collectors.toList())
                    )
            );
        }
    }
}
