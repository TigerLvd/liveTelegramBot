package model.homeGroups;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.Date;

import model.homeGroups.db.StatInfo;
import model.homeGroups.db.User;

public interface HomeGroupBotFacade {

    void sendUsersList(ReplyKeyboardMarkup keyboardMarkup, Long chatId);

    void sendNewUsersList(ReplyKeyboardMarkup keyboardMarkup, Long chatId);

    void sendHomeGroupsList(Long chatId, ReplyKeyboardMarkup keyboardMarkup, Long adminId);

    void sendLostStatInfos(Long chatId, User user, ReplyKeyboardMarkup keyboardMarkup, Boolean sendEmpty);

    void sendEnteredStatInfos(Long chatId, User user, ReplyKeyboardMarkup keyboardMarkup);

    void send(Long chatId, String str, ReplyKeyboardMarkup keyboard);

    void send(Long chatId, Integer messageId, String str, InlineKeyboardMarkup keyboard);

    void send(Long chatId, String str);

    StatInfo addStatInfo(Long chatId, User user, Date eventDate, Integer count);

    User addUser(Chat chat);

    void setBot(TelegramLongPollingBot homeGroupBot);

    void sendInfoAbout(Long chatId, Long userId, ReplyKeyboardMarkup keyboardMarkup);
}
