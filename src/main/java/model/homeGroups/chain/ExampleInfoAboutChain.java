package model.homeGroups.chain;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.regex.Pattern;

import model.homeGroups.HomeGroupBotFacade;
import model.homeGroups.db.User;

public class ExampleInfoAboutChain extends Chain {
    private final Pattern pattern = Pattern.compile("Инфа\\s*по", Pattern.CASE_INSENSITIVE);

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public void doJob(final HomeGroupBotFacade homeGroupBotFacade, final Long chatId, final String text, final User user, final ReplyKeyboardMarkup keyboardMarkup, Long adminId) {
        homeGroupBotFacade.send(chatId, "Введите команду в формате: Инфа по "
                + "<id пользователя> (id пользователей можно посмотреть в списке пользователей - нопка \"Пользователи\"). Например:", keyboardMarkup);
        homeGroupBotFacade.send(chatId, "Инфа по " + user.getId(), keyboardMarkup);
    }

    public boolean check(String value, boolean hasHomeGroup, boolean isAdmin) {
        return isAdmin && getPattern().matcher(value).matches();
    }
}
