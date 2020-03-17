package model.homeGroups.chain;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.regex.Pattern;

import model.homeGroups.HomeGroupBotFacade;
import model.homeGroups.db.User;

public class HomeGroupListChain extends Chain {
    private final Pattern pattern = Pattern.compile("Список\\s*ячеек", Pattern.CASE_INSENSITIVE);

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public void doJob(final HomeGroupBotFacade homeGroupBotFacade, final Long chatId, final String text, final User user, final ReplyKeyboardMarkup keyboardMarkup, final Long adminId) {
        homeGroupBotFacade.sendHomeGroupsList(chatId, keyboardMarkup, adminId);
    }
}
