package model.homeGroups.chain;

import model.homeGroups.db.HomeGroup;
import model.homeGroups.facade.BotFacade;
import model.homeGroups.facade.DBFacade;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import model.homeGroups.db.User;
import utils.Utils;

public class ShowHomeGroupListChain extends Chain {
    private final Pattern pattern = Pattern.compile("\\s*Список\\s\\s*ячеек\\s*", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean check(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        return pattern.matcher(message.getText()).matches();
    }

    @Override
    public void doJob(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        List<HomeGroup> groups = dbFacade.getHomeGroupService().findAll(HomeGroup.LIEDER_FIELD);
        botFacade.sendMessageByBlocks(message.getChatId(), buildBlocks(atr, groups), buildKeyboardForUser((User) atr.get(USER_FIELD)));
    }

    private List<String> buildBlocks(Map<String, Object> atr, List<HomeGroup> groups) {
        List<String> result = new ArrayList<>();
        if (Utils.isEmpty(groups)) {
            result.add("Пусто");
            return result;
        }

        int i = 1;
        for (HomeGroup group : groups) {
            StringBuilder groupsInfos = new StringBuilder();
            groupsInfos.append(i++);
            groupsInfos.append(") ");
            if (null != group.getLieder() && (null != group.getLieder().getLastName() || null != group.getLieder().getFirstName())) {
                groupsInfos.append("лидер: ");
                if (null != group.getLieder().getLastName()) {
                    groupsInfos.append(group.getLieder().getLastName());
                    groupsInfos.append(" ");
                }
                if (null != group.getLieder().getFirstName()) {
                    groupsInfos.append(group.getLieder().getFirstName());
                }
                groupsInfos.append(", ");
            }
            if (isFieldAndTrue(atr.get(IS_ADMIN_FIELD))) {
                groupsInfos.append("(");
                groupsInfos.append(group.getComment());
                groupsInfos.append("), ");
            }
            if (Utils.isField(group.getLiederPhoneNumber())) {
                groupsInfos.append("номер лидера: ");
                groupsInfos.append(formatPhoneNumber(group.getLiederPhoneNumber()));
                groupsInfos.append(", ");
            }
            if (Utils.isField(group.getLieder()) && Utils.isField(group.getLieder().getNickName())) {
                groupsInfos.append("telegram: @");
                groupsInfos.append(group.getLieder().getNickName());
                groupsInfos.append(", ");
            }
            groupsInfos.append("адрес: ");
            groupsInfos.append(group.getAddress());
            groupsInfos.append(", время: ");
            groupsInfos.append(group.getDayOfTheWeek().getTitle());
            groupsInfos.append(" в ");
            groupsInfos.append(group.getTime());
            groupsInfos.append("\n\n");

            result.add(groupsInfos.toString());
        }
        return result;
    }

    private String formatPhoneNumber(String str) {
        return "+7 (" + str.substring(0, 3) + ") " + str.substring(3, 6) +  " " + str.substring(6, 8) + " " + str.substring(8, 10);
    }
}
