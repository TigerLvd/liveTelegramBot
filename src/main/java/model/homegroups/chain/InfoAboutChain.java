package model.homegroups.chain;

import model.homegroups.db.*;
import model.homegroups.facade.*;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import utils.Utils;

import java.util.*;
import java.util.regex.Pattern;

public class InfoAboutChain extends Chain {
    private final Pattern pattern = Pattern.compile("\\s*Инфа\\s\\s*по\\s\\s*\\d*\\s*", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    @Override
    public boolean check(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        return pattern.matcher(message.getText()).matches();
    }

    @Override
    public void doJob(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        String[] inputStrings = message.getText().split("Инфа по ");
        User userInfo = dbFacade.getUserService().findById(Long.valueOf(inputStrings[1].trim()));
        User user = (User) atr.get(USER_FIELD);
        botFacade.sendMessageByBlocks(message.getChatId(), buildMessage(dbFacade, userInfo), buildKeyboardForUser(user));
    }

    private List<String> buildMessage(DBFacade dbFacade, User userInfo) {
        List<String> result = new ArrayList<>();
        if (Utils.isEmpty(userInfo)) {
            result.add("Нет пользователя с указанным id");
            return result;
        }

        String userInfos = "admin=" + userInfo.isAdmin() +
                ", id=" +  userInfo.getId() +
                ", chatId=" +  userInfo.getTelegramUserId() +
                ", firstName=" + userInfo.getFirstName() +
                ", lastName=" + userInfo.getLastName() +
                ", nickName=" + userInfo.getNickName() +
                ", comment=" + userInfo.getComment() +
                ", isLeader=" + userInfo.isLeader();
        result.add(userInfos);

        if (userInfo.hasHomeGroup()) {
            List<StatInfo> allStatInfos = dbFacade.getStatInfoService().findAllByHomeGroupId(userInfo.getHomeGroup().getId());
            if (Utils.isField(allStatInfos)) {
                result.add("\n\nВведено статистики:\n\n");

                int i = 1;
                for (StatInfo statInfo : allStatInfos) {
                    String stringBuilder = i++ + ") " +
                            statInfo.getCount() + " за " + Utils.getStringOfDate(statInfo.getEventDate()) + "\n\n";
                    result.add(stringBuilder);
                }
            } else {
                result.add("\n\nНе введено статистики");
            }
        } else {
            result.add("\n\nЯчейка не привязана к указанному пользователю");
        }
        return result;
    }
}
