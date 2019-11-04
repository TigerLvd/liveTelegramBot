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
import java.util.Iterator;
import java.util.List;

public class LiveInfoBot extends TelegramLongPollingBot {
    final int COLUMN_NUMBER = 2;
    final boolean IS_CHANGE_CURRENT_MESSAGE = false;

    private String botToken;
    private String botName;

    public LiveInfoBot(String botToken, String botName, DefaultBotOptions options) {
        super(options);
        this.botToken = botToken;
        this.botName = botName;
    }

    public LiveInfoBot(String botToken, String botName) {
        super();
        this.botToken = botToken;
        this.botName = botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText().toLowerCase();

            sendNewMessage(chatId, text);
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();

            if (IS_CHANGE_CURRENT_MESSAGE) {
                editCurrentMessage(callbackData, chatId, messageId);
            } else {
                sendNewMessage(chatId, callbackData);
            }
        }
    }

    private void editCurrentMessage(String callData, Long chatId, Integer messageId) {
        if (callData == null || callData.isEmpty()) {
            return;
        }

        Block block = Block.getByCode(callData);
        if (null != block) {
            editCurrentMessage(chatId, messageId, block);
        } else {
            Section section = Section.getByCode(callData);
            if (null != section) {
                editCurrentMessage(chatId, messageId, section);
            } else {
                Field field = Field.getByCode(callData);
                if (null != field) {
                    editCurrentMessage(chatId, messageId, field);
                } else {
                    editCurrentMessage(chatId, messageId, Block.GENERAL_MENU);
                }
            }
        }
    }

    private void sendNewMessage(Long chatId, String text) {
        if (null == text || text.isEmpty()) {
            return;
        }

        sendStartTestMessage(chatId, text);

        Block block = Block.getByCode(text);
        if (null != block) {
            sendNewMessage(chatId, block);
        } else {
            Section section = Section.getByCode(text);
            if (null != section) {
                sendNewMessage(chatId, section);
            } else {
                Field field = Field.getByCode(text);
                if (null != field) {
                    sendNewMessage(chatId, field);
                } else {
                    sendNewMessage(chatId, Block.GENERAL_MENU);
                }
            }
        }
    }

    private void sendStartTestMessage(Long chatId, String text) {
        if (text.equals("/start")) {
            SendMessage message = new SendMessage()
                    .setChatId(chatId)
                    .setText("Привет! Это чат бот и он пока на этапе тестирования!");
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void editCurrentMessage(Long chatId, Integer messageId, Block block) {
        EditMessageText message = new EditMessageText()
                .setChatId(chatId)
                .setMessageId(messageId)
                .setText(block.getName());
        InlineKeyboardMarkup markupInline = getInlineKeyboardMarkup(block);
        message.setReplyMarkup(markupInline);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void editCurrentMessage(Long chatId, Integer messageId, Section section) {
        EditMessageText message = new EditMessageText()
                .setChatId(chatId)
                .setMessageId(messageId)
                .setText(section.getName());
        InlineKeyboardMarkup markupInline = getInlineKeyboardMarkup(section);
        message.setReplyMarkup(markupInline);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void editCurrentMessage(Long chatId, Integer messageId, Field field) {
        EditMessageText message = new EditMessageText()
                .setChatId(chatId)
                .setMessageId(messageId)
                .setText(field.getName());
        InlineKeyboardMarkup markupInline = getInlineKeyboardMarkup(field);
        message.setReplyMarkup(markupInline);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendNewMessage(Long chatId, Field field) {
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(field.getName());
        InlineKeyboardMarkup markupInline = getInlineKeyboardMarkup(field);
        message.setReplyMarkup(markupInline);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendNewMessage(Long chatId, Section section) {
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(section.getName());
        InlineKeyboardMarkup markupInline = getInlineKeyboardMarkup(section);
        message.setReplyMarkup(markupInline);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendNewMessage(Long chatId, Block block) {
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(block.getName());
        InlineKeyboardMarkup markupInline = getInlineKeyboardMarkup(block);
        message.setReplyMarkup(markupInline);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkup(Section section) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<List<InlineKeyboardButton>>();
        List<InlineKeyboardButton> rowInline = new ArrayList<InlineKeyboardButton>();

        Iterator<Field> fieldIterator = section.getFields().iterator();
        int i = 0;
        while (fieldIterator.hasNext()) {
            Field field = fieldIterator.next();
            rowInline.add(new InlineKeyboardButton().setText(field.getName()).setCallbackData(field.getCode()));
            i++;
            if (i % COLUMN_NUMBER == 0) {
                rowsInline.add(rowInline);
                rowInline = new ArrayList<InlineKeyboardButton>();
                i = 0;
            }
        }
        if (!rowInline.isEmpty()) {
            rowsInline.add(rowInline);
            rowInline = new ArrayList<InlineKeyboardButton>();
        }
        Block block = section.getBlock();
        rowInline.add(new InlineKeyboardButton().setText(block.getName()).setCallbackData(block.getCode()));
        rowsInline.add(rowInline);

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkup(Field field) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<List<InlineKeyboardButton>>();
        List<InlineKeyboardButton> rowInline = new ArrayList<InlineKeyboardButton>();

        Section section = field.getSection();
        Block block = section.getBlock();
        rowInline.add(new InlineKeyboardButton().setText(section.getName()).setCallbackData(section.getCode()));
        rowInline.add(new InlineKeyboardButton().setText(block.getName()).setCallbackData(block.getCode()));
        rowsInline.add(rowInline);

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkup(Block block) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<List<InlineKeyboardButton>>();
        List<InlineKeyboardButton> rowInline = new ArrayList<InlineKeyboardButton>();

        Iterator<Section> sectionIterator = block.getSections().iterator();
        int i = 0;
        while (sectionIterator.hasNext()) {
            Section section = sectionIterator.next();
            rowInline.add(new InlineKeyboardButton().setText(section.getName()).setCallbackData(section.getCode()));
            i++;
            if (i % COLUMN_NUMBER == 0) {
                rowsInline.add(rowInline);
                rowInline = new ArrayList<InlineKeyboardButton>();
                i = 0;
            }
        }
        if (!rowsInline.isEmpty()) {
            rowsInline.add(rowInline);
        }

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
