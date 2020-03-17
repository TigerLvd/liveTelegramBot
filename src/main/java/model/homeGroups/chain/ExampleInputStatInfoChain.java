package model.homeGroups.chain;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.Date;
import java.util.regex.Pattern;

import model.homeGroups.HomeGroupBotFacade;
import model.homeGroups.db.User;

public class ExampleInputStatInfoChain extends Chain {
    private final Pattern pattern = Pattern.compile("\\s*Ввод\\s*статистики\\s*", Pattern.CASE_INSENSITIVE);

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public void doJob(final HomeGroupBotFacade homeGroupBotFacade, final Long chatId, final String text, final User user, final ReplyKeyboardMarkup keyboardMarkup, Long adminId) {
        String msg = "Введите информацию в формате: Статистика: дд.мм.гг количество.";
        String msg2 = "Обратите внимание, что слово \"<b>Статистика</b>\" и символ\"<b>:</b>\" должны быть в отправляемом сообщении!\n\n" +
                "Для удобства можно скопировать и изменить пример:";
        String msg3 = "Статистика: " + getStringOfDate(new Date()) + " 5";
        homeGroupBotFacade.send(chatId, msg, keyboardMarkup);
        homeGroupBotFacade.send(chatId, msg2, keyboardMarkup);
        homeGroupBotFacade.send(chatId, msg3, keyboardMarkup);
    }

    public boolean check(String value, boolean hasHomeGroup, boolean isAdmin) {
        return hasHomeGroup && getPattern().matcher(value).matches();
    }
}
