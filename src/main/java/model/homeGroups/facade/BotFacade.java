package model.homeGroups.facade;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class BotFacade {
    private TelegramLongPollingBot bot;
    private String pswrd;
    private String botFullName;
    private String botShortName;

    public void init(TelegramLongPollingBot bot, String pswrd, String botFullName) {
        this.bot = bot;
        this.pswrd = pswrd;
        this.botFullName = botFullName;
        this.botShortName = botFullName.substring(5);
    }

    public TelegramLongPollingBot getBot() {
        return bot;
    }

    public String getPswrd() {
        return pswrd;
    }

    public void sendMsg(SendMessage message) {
        try {
            message.enableHtml(true);
            bot.execute(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(final Long chatId, final String msg) {
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .enableHtml(true)
                .setText(msg);
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(final Long chatId, final String msg, final InlineKeyboardMarkup markupInline) {
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(msg)
                .enableHtml(true)
                .setReplyMarkup(markupInline);
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void updateMsg(final Long chatId, final String msg, Integer messageId, final InlineKeyboardMarkup markupInline) {
        EditMessageText message = new EditMessageText()
                .setChatId(chatId)
                .setMessageId(messageId)
                .setText(msg)
                .enableHtml(true)
                .setReplyMarkup(markupInline);
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void updateMsg(final Long chatId, final String msg, Integer messageId) {
        EditMessageText message = new EditMessageText()
                .setChatId(chatId)
                .setMessageId(messageId)
                .enableHtml(true)
                .setText(msg);
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String getBotFullName() {
        return botFullName;
    }

    public String getBotShortName() {
        return botShortName;
    }
}
