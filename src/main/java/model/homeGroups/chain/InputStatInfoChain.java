package model.homeGroups.chain;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.Date;
import java.util.regex.Pattern;

import model.homeGroups.HomeGroupBotFacade;
import model.homeGroups.db.StatInfo;
import model.homeGroups.db.User;

public class InputStatInfoChain extends Chain {
    private final Pattern pattern = Pattern.compile("\\s*Статистика:\\s*(\\d|\\d{2})\\.(\\d|\\d{2})\\.(\\d{4}|\\d{2})\\s*\\d\\d*\\s*", Pattern.CASE_INSENSITIVE);

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public void doJob(final HomeGroupBotFacade homeGroupBotFacade, final Long chatId, final String text, final User user, final ReplyKeyboardMarkup keyboardMarkup, Long adminId) {
        String[] str;
        Date date;
        try {
            str = text.split(" ");
            String[] sbStr = str[1].split("\\.");

            date = getDate(sbStr[0], sbStr[1], sbStr[2]);
        } catch (Exception e) {
            String msg = "Не верный формат! Ввести информацию в формате: Статистика: ДД.ММ.ГГ количество. Например:";
            String msg2 = "Статистика: " + getStringOfDate(new Date()) + " 5";
            homeGroupBotFacade.send(chatId, msg, keyboardMarkup);
            homeGroupBotFacade.send(chatId, msg2, keyboardMarkup);
            return;
        }
        Integer count;
        try {
            count = new Integer(str[2]);
        } catch (Exception e) {
            String msg = "Не верный формат! Ввести информацию в формате: Статистика: дд.мм.гг КОЛИЧЕСТВО. Например:";
            String msg2 = "Статистика: " + getStringOfDate(new Date()) + " 5";
            homeGroupBotFacade.send(chatId, msg, keyboardMarkup);
            homeGroupBotFacade.send(chatId, msg2, keyboardMarkup);
            return;
        }

        StatInfo statInfo = homeGroupBotFacade.addStatInfo(chatId, user, date, count);

        homeGroupBotFacade.send(chatId, "Получено: " + count + " за " + getStringOfDate(statInfo.getEventDate()), keyboardMarkup);
    }

    public boolean check(String value, boolean hasHomeGroup, boolean isAdmin) {
        return hasHomeGroup && getPattern().matcher(value).matches();
    }
}
