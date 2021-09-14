package model.liveInfoAdmin;

import model.CustomKeyboardMarkup;
import model.liveInfo.db.Field;
import model.liveInfo.services.FieldService;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class LiveInfoAdminBot extends TelegramLongPollingBot {

    private final Long adminId;
    private final String botToken;
    private final String botName;

    private final FieldService fieldService;

    public LiveInfoAdminBot(String botToken, String botName, DefaultBotOptions options, FieldService fieldService, Long adminId) {
        super(options);
        this.botToken = botToken;
        this.botName = botName;
        this.fieldService = fieldService;
        this.adminId = adminId;
    }

    public LiveInfoAdminBot(String botToken, String botName, FieldService fieldService, Long adminId) {
        super();
        this.botToken = botToken;
        this.botName = botName;
        this.fieldService = fieldService;
        this.adminId = adminId;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText();

            sendNewMessage(chatId, text);
        }
    }

    private void sendNewMessage(Long chatId, String text) {
        if (null == text || text.isEmpty() || !chatId.equals(adminId)) {
            return;
        }

        CustomKeyboardMarkup keyboardMarkup = new CustomKeyboardMarkup("все id", "показать id=", "показать <name>",
                "поменять id= text=", "поменять id= parentId=", "поменять id= text=", "поменять id= name=",
                "поменять id= latitude=", "поменять id= longitude=", "поменять id= photoPath=",
                "поменять id= columnCount=", "поменять id= isShowBrothers=", "создать");
        String res = "Введи команду:";

        if (text.startsWith("показать")) {
            String[] showStrs = text.split("показать ");
            String showStr = showStrs[1];
            if (showStr != null) {
                Field field;

                if (!showStr.equals("id=") && showStr.startsWith("id=")) {
                    String[] idStrs = showStr.split("id=");
                    String idStr = idStrs[1];
                    field = fieldService.findById(Long.valueOf(idStr.trim()));
                } else {
                    field = fieldService.findByName(showStr.trim());
                }

                if (field != null) {
                    res = getFieldValues(field);
                }

                if (field != null && field.getLatitude() != null) {
                    SendLocation location = new SendLocation()
                            .setChatId(chatId)
                            .setLongitude(field.getLongitude())
                            .setLatitude(field.getLatitude())
                            .setReplyMarkup(keyboardMarkup);
                    try {
                        execute(location);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if ("все id".equals(text)) {
            List<Field> all = fieldService.findAll();
            StringBuilder ids = new StringBuilder();
            for (Field field : all) {
                ids.append(field.getId()).append("\t");
            }
            res = ids.toString();
        } else if ("создать".equals(text)) {
            Field field = fieldService.findByName("_EMPTY_NAME_");
            if (field != null) {
                res = "Уже есть на задестованный:" + getFieldValues(field);
            } else {
                field = new Field();
                field.setName("_EMPTY_NAME_");
                field.setColumnCount(2L);

                fieldService.saveOrUpdate(field);

                field = fieldService.findByName("_EMPTY_NAME_");
                res = "Создано:\n" + getFieldValues(field);
            }
        } else if (text.startsWith("поменять ")) {
            String addStr = text.split("поменять ")[1];
            if (addStr != null) {
                Field field = null;

                if (addStr.contains("id=")) {
                    if (addStr.contains("parentId=")) {
                        String[] split = addStr.split("id=")[1].split("parentId=");
                        Long id = Long.valueOf(split[0].trim());
                        Long parentId = "null".equals(split[1].trim()) ? null : Long.valueOf(split[1].trim());
                        fieldService.updateParentId(id, parentId);
                        field = fieldService.findById(id);
                    } else if (addStr.contains("text=")) {
                        String[] split = addStr.split("id=")[1].split("text=");
                        Long id = Long.valueOf(split[0].trim());
                        String textValue = "null".equals(split[1].trim()) ? null : split[1].trim();
                        fieldService.updateText(id, textValue);
                        field = fieldService.findById(id);
                    } else if (addStr.contains("name=")) {
                        String[] split = addStr.split("id=")[1].split("name=");
                        Long id = Long.valueOf(split[0].trim());
                        String nameValue = "null".equals(split[1].trim()) ? null : split[1].trim();
                        fieldService.updateName(id, nameValue);
                        field = fieldService.findById(id);
                    } else if (addStr.contains("latitude=")) {///////////////
                        String[] split = addStr.split("id=")[1].split("latitude=");
                        Long id = Long.valueOf(split[0].trim());
                        Float latitude = "null".equals(split[1].trim()) ? null : Float.valueOf(split[1].trim());
                        fieldService.updateLatitude(id, latitude);
                        field = fieldService.findById(id);
                    } else if (addStr.contains("longitude=")) {
                        String[] split = addStr.split("id=")[1].split("longitude=");
                        Long id = Long.valueOf(split[0].trim());
                        Float longitude = "null".equals(split[1].trim()) ? null : Float.valueOf(split[1].trim());
                        fieldService.updateLongitude(id, longitude);
                        field = fieldService.findById(id);
                    } else if (addStr.contains("photoPath=")) {
                        String[] split = addStr.split("id=")[1].split("photoPath=");
                        Long id = Long.valueOf(split[0].trim());
                        String photoPath = "null".equals(split[1].trim()) ? null : split[1].trim();
                        fieldService.updatePhotoPath(id, photoPath);
                        field = fieldService.findById(id);
                    } else if (addStr.contains("columnCount=")) {
                        String[] split = addStr.split("id=")[1].split("columnCount=");
                        Long id = Long.valueOf(split[0].trim());
                        Long columnCount = "null".equals(split[1].trim()) ? 2 : Long.valueOf(split[1].trim());
                        fieldService.updateColumnCount(id, columnCount);
                        field = fieldService.findById(id);
                    } else if (addStr.contains("isShowBrothers=")) {
                        String[] split = addStr.split("id=")[1].split("isShowBrothers=");
                        Long id = Long.valueOf(split[0].trim());
                        boolean isShowBrothers = "true".equals(split[1].trim()) || "1".equals(split[1].trim());
                        fieldService.updateIsShowBrothers(id, isShowBrothers);
                        field = fieldService.findById(id);
                    } else {
                        res = "Ошибка команды";
                    }
                }

                if (field != null) {
                    res = getFieldValues(field);
                }

                if (field != null && field.getLatitude() != null) {
                    SendLocation location = new SendLocation()
                            .setChatId(chatId)
                            .setLongitude(field.getLongitude())
                            .setLatitude(field.getLatitude())
                            .setReplyMarkup(keyboardMarkup);
                    try {
                        execute(location);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            //
        }

        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(res)
                .setReplyMarkup(keyboardMarkup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String getFieldValues(Field field) {
        return "Id=" + field.getId() + "\n" +
                "ParentId=" + field.getParentId() + "\n" +
                "Text=<" + field.getText() + ">\n" +
                "Name=<" + field.getName() + ">\n" +
                "Latitude=" + field.getLatitude() + "\n" +
                "Longitude=" + field.getLongitude() + "\n" +
                "PhotoPath=<" + field.getPhotoPath() + ">\n" +
                "ColumnCount=" + field.getColumnCount() + "\n" +
                "isShowBrothers=" + field.isShowBrothers();
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
