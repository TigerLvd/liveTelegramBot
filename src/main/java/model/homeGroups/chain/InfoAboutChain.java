package model.homeGroups.chain;

import model.homeGroups.db.StatInfo;
import model.homeGroups.db.User;
import model.homeGroups.facade.BotFacade;
import model.homeGroups.facade.DBFacade;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import utils.Utils;

import java.util.*;
import java.util.regex.Pattern;

public class InfoAboutChain extends Chain {
    private final Pattern pattern = Pattern.compile("\\s*Инфа\\s\\s*по\\s\\s*\\d*\\s*", Pattern.CASE_INSENSITIVE);

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

        StringBuilder userInfos = new StringBuilder();
        userInfos.append("admin=");
        userInfos.append(userInfo.isAdmin());
        userInfos.append(", ");
        userInfos.append("id=");
        userInfos.append(userInfo.getId());
        userInfos.append(", chatId=");
        userInfos.append(userInfo.getTelegramUserId());
        userInfos.append(", firstName=");
        userInfos.append(userInfo.getFirstName());
        userInfos.append(", lastName=");
        userInfos.append(userInfo.getLastName());
        userInfos.append(", nickName=");
        userInfos.append(userInfo.getNickName());
        userInfos.append(", comment=");
        userInfos.append(userInfo.getComment());
        userInfos.append(", isLeader=");
        userInfos.append(userInfo.isLeader());
        result.add(userInfos.toString());

        if (userInfo.hasHomeGroup()) {
            List<StatInfo> allStatInfos = dbFacade.getStatInfoService().findAllByHomeGroupId(userInfo.getHomeGroup().getId());
            if (Utils.isField(allStatInfos)) {
                result.add("\n\nВведено статистики:\n\n");

                int i = 1;
                for (StatInfo statInfo : allStatInfos) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(i++);
                    stringBuilder.append(") ");
                    stringBuilder.append(statInfo.getCount());
                    stringBuilder.append(" за ");
                    stringBuilder.append(Utils.getStringOfDate(statInfo.getEventDate()));
                    stringBuilder.append("\n\n");
                    result.add(stringBuilder.toString());
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
