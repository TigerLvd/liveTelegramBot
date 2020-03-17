package model.homeGroups.chain;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.regex.Pattern;

import model.homeGroups.HomeGroupBotFacade;
import model.homeGroups.db.User;

public class NewUsersChain extends Chain {
    private final Pattern pattern = Pattern.compile("Новые\\s*пользователи", Pattern.CASE_INSENSITIVE);

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public void doJob(final HomeGroupBotFacade homeGroupBotFacade, final Long chatId, final String text, final User user, final ReplyKeyboardMarkup keyboardMarkup, Long adminId) {
        homeGroupBotFacade.sendNewUsersList(keyboardMarkup, adminId);
    }

    public boolean check(String value, boolean hasHomeGroup, boolean isAdmin) {
        return isAdmin && getPattern().matcher(value).matches();
    }
}
