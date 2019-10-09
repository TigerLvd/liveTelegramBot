package model.liveInfo;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class LiveInfoBot extends TelegramLongPollingBot {
    private String botToken;
    private String botName;

    public LiveInfoBot(String botToken, String botName, DefaultBotOptions options) {
        super(options);
        this.botToken = botToken;
        this.botName = botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText().toLowerCase();
            if (null == text || text.isEmpty()) {
                return;
            }

            if (text.equals("расписание")) {
                sendTimeTable(chatId);
            } else if (text.equals("соц.сети")) {
                sendSocialNetwork(chatId);
            } else if (text.equals("зимняя конференция live")) {
                sendConfInfo(chatId);
            } else {
                sendMenu(chatId);
            }
        } else if (update.hasCallbackQuery()) {
            String callData = update.getCallbackQuery().getData();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
            if (callData == null || callData.isEmpty()) {
                return;
            }
            if (callData.equals("time")) {
                editTimeTable(chatId, messageId);
            } else if (callData.equals("messagers")) {
                editSocialNetwork(chatId, messageId);
            } else if (callData.equals("conf")) {
                editConfInfo(chatId, messageId);
            } else {
                editMenu(chatId, messageId);
            }
        }
    }

    private void sendMenu(Long chatId) {
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText("Главное меню");
        InlineKeyboardMarkup markupInline = getInlineKeyboardMarkupGeneralMenu();
        message.setReplyMarkup(markupInline);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void editMenu(Long chatId, Integer messageId) {
        EditMessageText message = new EditMessageText()
                .setChatId(chatId)
                .setMessageId(messageId)
                .setText("Главное меню");
        InlineKeyboardMarkup markupInline = getInlineKeyboardMarkupGeneralMenu();
        message.setReplyMarkup(markupInline);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendConfInfo(Long chatId) {
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText("Зимняя конференция live");
        InlineKeyboardMarkup markupInline = getInlineKeyboardMarkupConfInfo();
        message.setReplyMarkup(markupInline);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void editConfInfo(Long chatId, Integer messageId) {
        EditMessageText message = new EditMessageText()
                .setChatId(chatId)
                .setMessageId(messageId)
                .setText("Зимняя конференция live");
        InlineKeyboardMarkup markupInline = getInlineKeyboardMarkupConfInfo();
        message.setReplyMarkup(markupInline);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendSocialNetwork(Long chatId) {
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText("Соц.сети");
        InlineKeyboardMarkup markupInline = getInlineKeyboardMarkupSocialNetwork();
        message.setReplyMarkup(markupInline);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void editSocialNetwork(Long chatId, Integer messageId) {
        EditMessageText message = new EditMessageText()
                .setChatId(chatId)
                .setMessageId(messageId)
                .setText("Соц.сети");
        InlineKeyboardMarkup markupInline = getInlineKeyboardMarkupSocialNetwork();
        message.setReplyMarkup(markupInline);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendTimeTable(Long chatId) {
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText("Рассписание");
        InlineKeyboardMarkup markupInline = getInlineKeyboardMarkupTimeTable();
        message.setReplyMarkup(markupInline);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void editTimeTable(Long chatId, Integer messageId) {
        EditMessageText message = new EditMessageText()
                .setChatId(chatId)
                .setMessageId(messageId)
                .setText("Рассписание");
        InlineKeyboardMarkup markupInline = getInlineKeyboardMarkupTimeTable();
        message.setReplyMarkup(markupInline);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkupGeneralMenu() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<List<InlineKeyboardButton>>();
        List<InlineKeyboardButton> rowInline = new ArrayList<InlineKeyboardButton>();
        rowInline.add(new InlineKeyboardButton().setText("Рассписание").setCallbackData("time"));
        rowInline.add(new InlineKeyboardButton().setText("Соц.сети").setCallbackData("messagers"));
        rowInline.add(new InlineKeyboardButton().setText("Зимняя конференция live").setCallbackData("conf"));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkupConfInfo() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<List<InlineKeyboardButton>>();
        List<InlineKeyboardButton> rowInline = new ArrayList<InlineKeyboardButton>();
        rowInline.add(new InlineKeyboardButton().setText("Даты").setCallbackData("confDays"));
        rowInline.add(new InlineKeyboardButton().setText("Регистрация").setCallbackData("registration"));
        rowInline.add(new InlineKeyboardButton().setText("Расселение").setCallbackData("home"));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkupSocialNetwork() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<List<InlineKeyboardButton>>();
        List<InlineKeyboardButton> rowInline = new ArrayList<InlineKeyboardButton>();
        rowInline.add(new InlineKeyboardButton().setText("instagram").setCallbackData("instagram"));
        rowInline.add(new InlineKeyboardButton().setText("vk").setCallbackData("vk"));
        rowInline.add(new InlineKeyboardButton().setText("telegram").setCallbackData("telegram"));
        rowsInline.add(rowInline);

        rowInline = new ArrayList<InlineKeyboardButton>();
        rowInline.add(new InlineKeyboardButton().setText("youtube").setCallbackData("youtube"));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkupTimeTable() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<List<InlineKeyboardButton>>();
        List<InlineKeyboardButton> rowInline = new ArrayList<InlineKeyboardButton>();
        rowInline.add(new InlineKeyboardButton().setText("Субботнее служение").setCallbackData("saturdayService"));
        rowInline.add(new InlineKeyboardButton().setText("Первое воскресное служение").setCallbackData("firstService"));
        rowInline.add(new InlineKeyboardButton().setText("Второе воскресное служение").setCallbackData("secondService"));
        rowsInline.add(rowInline);

        rowInline = new ArrayList<InlineKeyboardButton>();
        rowInline.add(new InlineKeyboardButton().setText("Препати").setCallbackData("preparety"));
        rowInline.add(new InlineKeyboardButton().setText("Молодёжное служение").setCallbackData("youthService"));
        rowInline.add(new InlineKeyboardButton().setText("Автопати").setCallbackData("afterparty"));

        rowInline = new ArrayList<InlineKeyboardButton>();
        rowInline.add(new InlineKeyboardButton().setText("Утренняя молитва").setCallbackData("morningPrayer"));
        rowInline.add(new InlineKeyboardButton().setText("Ночная молитва").setCallbackData("nightPrayer"));
        rowInline.add(new InlineKeyboardButton().setText("Домашние группы").setCallbackData("homeGroups"));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
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
