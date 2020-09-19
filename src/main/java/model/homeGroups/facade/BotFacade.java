package model.homeGroups.facade;

import model.homeGroups.db.HomeGroup;
import model.homeGroups.db.StatInfo;
import model.homeGroups.db.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import utils.Utils;

import java.io.File;
import java.util.*;

public class BotFacade {
    private final String rootPath = "/";
    private final int MAX_COUNT_ITEMS_IN_MESSAGE = 10;
    private final boolean ENABLE_HTML = false;

    private TelegramLongPollingBot bot;
    private String botShortName;

    public TelegramLongPollingBot getBot() {
        return bot;
    }

    public String getBotShortName() {
        return botShortName;
    }

    public void init(TelegramLongPollingBot bot, String botFullName) {
        this.bot = bot;
        this.botShortName = botFullName.substring(5);
    }

    public void sendMsg(SendMessage message) {
        try {
            message.enableHtml(ENABLE_HTML);
            getBot().execute(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(final Long chatId, final String msg) {
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .enableHtml(ENABLE_HTML)
                .setText(msg);
        try {
            getBot().execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(final Long chatId, final String msg, final ReplyKeyboard replyKeyboard) {
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(msg)
                .enableHtml(ENABLE_HTML)
                .setReplyMarkup(replyKeyboard);
        try {
            getBot().execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageByBlocks(final Long chatId, List<String> msgs, ReplyKeyboardMarkup replyKeyboard) {
        int index = 0;
        while (index < msgs.size()) {
            String text = String.join("", msgs.subList(index, getNextIndex(index, msgs.size())));
            sendMsg(chatId, text, replyKeyboard);
            index = getNextIndex(index, msgs.size());
        }
    }

    private int getNextIndex(int index, int maxIndex) {
        return Math.min(index + MAX_COUNT_ITEMS_IN_MESSAGE, maxIndex);
    }

    public void updateMsg(final Long chatId, final String msg, Integer messageId, final InlineKeyboardMarkup markupInline) {
        EditMessageText message = new EditMessageText()
                .setChatId(chatId)
                .setMessageId(messageId)
                .setText(msg)
                .enableHtml(ENABLE_HTML)
                .setReplyMarkup(markupInline);
        try {
            getBot().execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void updateMsg(final Long chatId, final String msg, Integer messageId) {
        EditMessageText message = new EditMessageText()
                .setChatId(chatId)
                .setMessageId(messageId)
                .enableHtml(ENABLE_HTML)
                .setText(msg);
        try {
            getBot().execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendFile(Long chatId, String path, ReplyKeyboardMarkup keyboardMarkup) {
        SendDocument document = new SendDocument()
                .setChatId(chatId)
                .setDocument(new File(rootPath + path))
                .setReplyMarkup(keyboardMarkup);
        try {
            getBot().execute(document);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            sendMsg(chatId, "Системная ошибка", keyboardMarkup);
        }
    }

    public void sendLostStatInfos(DBFacade dbFacade, Long chatId, User user, ReplyKeyboardMarkup keyboardMarkup, Boolean sendEmpty) {
        List<Date> lostWeeks = getLostWeeks(dbFacade, user.getHomeGroup());

        if (lostWeeks.isEmpty()) {
            if (!sendEmpty) {
                return;
            }
            sendMsg(chatId, "Все данные введены, задолженностей нет", keyboardMarkup);
        } else {
            StringBuilder result = new StringBuilder("Нет информации за следующие недели:\n");
            int i = 1;
            for (Date monday : lostWeeks) {
                result.append(i);
                result.append(") ");
                result.append(Utils.getMndToSunString(monday));
                result.append("\n");

                i++;
            }

            sendMsg(chatId, result.toString(), keyboardMarkup);
        }
    }

    /**
     * @param homeGroup ячейка по которой ищется информация
     * @return список понедельников у недель, где не было внесено информации о статистике, начиная с даты
     * включения ячейки в систему.
     */
    public List<Date> getLostWeeks(DBFacade dbFacade, HomeGroup homeGroup) {
        Map<Date, StatInfo> statInfoByFirstDayOfWeek = getMapStatInfoByFirstDayOfWeek(dbFacade, homeGroup.getId());
        Calendar start = getStartDate(homeGroup);
        List<Date> lostWeeks = new ArrayList<>();
        while (start.before(new GregorianCalendar())) {
            if (statInfoByFirstDayOfWeek.get(start.getTime()) == null) {
                lostWeeks.add(start.getTime());
            }
            start.add(Calendar.DAY_OF_MONTH, 7);
        }
        return lostWeeks;
    }

    public Map<Date, StatInfo> getMapStatInfoByFirstDayOfWeek(DBFacade dbFacade, Long homeGroupId) {
        Map<Date, StatInfo> statInfoByFirstDayOfWeek = new HashMap<>();
        List<StatInfo> allStatInfos = dbFacade.getStatInfoService().findAllByHomeGroupId(homeGroupId);
        if (null != allStatInfos) {
            for (StatInfo statInfo : allStatInfos) {
                Date time = Utils.getFirstDayOfWeek(statInfo.getEventDate());
                statInfoByFirstDayOfWeek.put(time, statInfo);
            }
        }
        return statInfoByFirstDayOfWeek;
    }

    public Calendar getStartDate(HomeGroup homeGroup) {
        Calendar start;
        if (homeGroup.getStartDate() != null) {
            start = new GregorianCalendar();
            start.setTime(homeGroup.getStartDate());
        } else {
            start = new GregorianCalendar();
        }

        start.setTime(Utils.getFirstDayOfWeek(start.getTime()));
        return start;
    }
}
