package model.liveInfo;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import model.CustomKeyboardMarkup;
import model.liveInfo.db.Field;
import model.liveInfo.services.FieldService;

public class LiveInfoBot extends TelegramLongPollingBot {

    private String botToken;
    private String botName;

    private FieldService fieldService;

    public LiveInfoBot(String botToken, String botName, DefaultBotOptions options, FieldService fieldService) {
        super(options);
        this.botToken = botToken;
        this.botName = botName;
        this.fieldService = fieldService;
    }

    public LiveInfoBot(String botToken, String botName, FieldService fieldService) {
        super();
        this.botToken = botToken;
        this.botName = botName;
        this.fieldService = fieldService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText().toLowerCase();

            sendNewMessage(chatId, text);
        }
    }

    private void sendNewMessage(Long chatId, String text) {
        if (null == text || text.isEmpty()) {
            return;
        }

        Field field = fieldService.findByName(text);
        if (null == field) {
            return;
        }

        sendStartTestMessage(chatId, text);

        List<Field> children = fieldService.findByParentId(field.getId());
        List<String> keyNames = new ArrayList<String>(0);
        if (null != children) {
            keyNames.addAll(Collections2.transform(children, new Function<Field, String>() {
                @Nullable
                @Override
                public String apply(Field field) {
                    return field.getName();
                }
            }));
        }
        addParents(field, keyNames);
        addIfNoContains(keyNames, "Расписание");
        addIfNoContains(keyNames, "Соц.сети");
        addIfNoContains(keyNames, "Зимняя конференция");

        CustomKeyboardMarkup keyboardMarkup = new CustomKeyboardMarkup(keyNames);
        if (field.hasPhoto()) {
            SendPhoto photo = new SendPhoto();
            photo.setPhoto(new File(field.getPhotoPath()));
            photo.setChatId(chatId);
            photo.setCaption(field.getText() == null || field.getText().isEmpty() ? field.getName() : field.getText());
            photo.setReplyMarkup(keyboardMarkup);
            photo.setParseMode("html");
            try {
                execute(photo);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
            SendMessage message = new SendMessage()
                    .setChatId(chatId)
                    .setText(field.getText() == null || field.getText().isEmpty() ? field.getName() : field.getText());
            message.enableHtml(true);
            message.setReplyMarkup(keyboardMarkup);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void addParents(Field field, List<String> keyNames) {
        Field parent = fieldService.findById(field.getParentId());
        while (null != parent) {
            addIfNoContains(keyNames, parent.getName());
            parent = fieldService.findById(parent.getParentId());
        }
    }

    private void addIfNoContains(List<String> keyNames, String name) {
        if (!keyNames.contains(name)) {
            keyNames.add(name);
        }
    }

    private void sendStartTestMessage(Long chatId, String text) {
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText("Привет! Это чат бот и он пока на этапе тестирования!");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
