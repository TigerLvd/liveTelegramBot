package model.liveInfo;

import com.google.common.collect.Collections2;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        CustomKeyboardMarkup keyboardMarkup = buildKeyboard(field);

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

    private CustomKeyboardMarkup buildKeyboard(Field field) {
        List<String> keyNames = new ArrayList<>();
        List<Field> allFields = fieldService.findAll();

        Map<Long, Field> fieldById = new HashMap<>();
        for (Field currentField : allFields) {
            fieldById.put(currentField.getId(), currentField);
        }

        Map<Long, List<Field>> fieldsByParentId = new HashMap<>();
        for (Field currentField : allFields) {
            fieldsByParentId.computeIfAbsent(currentField.getParentId(), k -> new ArrayList<>());
            fieldsByParentId.get(currentField.getParentId()).add(currentField);
        }

        List<Field> childes = fieldsByParentId.get(field.getId());
        if (null != childes && !childes.isEmpty()) {
            keyNames.addAll(Collections2.transform(childes, Field::getName));
        }

        if (null != field.parentId) {
            Field vsp = fieldById.get(field.parentId);

            List<Field> brothers = fieldsByParentId.get(vsp.getId());
            if (null != brothers && !brothers.isEmpty()) {
                keyNames.addAll(Collections2.transform(brothers, Field::getName));
            }

            while (null != vsp && null != vsp.parentId) {
                addIfNoContains(keyNames, vsp.getName());
                vsp = fieldById.get(vsp.parentId);
            }
        }

        addIfNoContains(keyNames, "Расписание мероприятий на неделе");
        addIfNoContains(keyNames, "Соц.сети");
        addIfNoContains(keyNames, "Зимняя конференция");

        return new CustomKeyboardMarkup(keyNames);
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
