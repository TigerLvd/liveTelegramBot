package model.homeGroups;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

import model.homeGroups.db.User;
import model.homeGroups.service.UserService;

@EnableScheduling
public class Scheduler {

    private UserService userService;

    private HomeGroupBotFacade homeGroupBotFacade;

    public void checkLostStatInfos() {

        List<User> allUsers = userService.findAll();
        for (User user : allUsers) {
            if (null == user.getTelegramUserId()) {
                continue;
            }

            ReplyKeyboardMarkup keyboardMarkup;
            if (user.isAdmin()) {
                keyboardMarkup = new CustomKeyboardMarkup("Пользователи", "Ввод статистики", "Не заполнено", "Настройка уведомлений", "Список ячеек");
            } else {
                keyboardMarkup = new CustomKeyboardMarkup("Ввод статистики", "Не заполнено", "Настройка уведомлений", "Список ячеек");
            }

            homeGroupBotFacade.sendLostStatInfos(user.getTelegramUserId(), user, keyboardMarkup, false);
        }
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public HomeGroupBotFacade getHomeGroupBotFacade() {
        return homeGroupBotFacade;
    }

    public void setHomeGroupBotFacade(HomeGroupBotFacade homeGroupBotFacade) {
        this.homeGroupBotFacade = homeGroupBotFacade;
    }
}
