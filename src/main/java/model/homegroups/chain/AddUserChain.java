package model.homegroups.chain;

import model.homegroups.db.User;
import model.homegroups.facade.BotFacade;
import model.homegroups.facade.DBFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import utils.Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class AddUserChain extends Chain {
    private static final Logger log = LoggerFactory.getLogger(Chain.class);

    private final Long adminId;
    private final String authPswrd;

    private String getAuthPswrd() {
        Properties property = new Properties();
        try (FileInputStream fis = new FileInputStream("src/main/resources/config.properties")) {
            property.load(fis);

            return property.getProperty("auth_pswrd");
        } catch (IOException e) {
            log.error("ОШИБКА: Файл config.properties отсуствует!");
        }
        return null;
    }

    public AddUserChain(Long adminId) {
        this.adminId = adminId;
        this.authPswrd = getAuthPswrd();
    }

    @Override
    public boolean check(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        return Utils.isEmpty(atr.get(HOME_GROUP_FIELD)) && Utils.isEmpty(atr.get(USER_FIELD))
                && Utils.isField(message) && message.hasText() && message.getText().equals(authPswrd);
    }

    @Override
    public void doJob(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        User user = dbFacade.getUserService().addUser(message.getChat());

        Long chatId = message.getChatId();
        botFacade.sendMsg(chatId, "Данные записаны: " + user.toString());
        botFacade.sendMsg(adminId, "Добавлен пользователь:" + user.toString());
        botFacade.sendMsg(chatId, "Заявка на добавление внесена, ожидайте принятия.");
    }
}
