package model.homeGroups.chain;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

import model.homeGroups.HomeGroupBotFacade;
import model.homeGroups.db.User;

public abstract class Chain {
    private Chain next = null;

    public boolean check(String value, boolean hasHomeGroup, boolean isAdmin) {
        return getPattern().matcher(value).matches();
    }

    public Chain getNext() {
        return next;
    }

    private void setNext(Chain next) {
        this.next = next;
    }

    public boolean hasNext() {
        return getNext() != null;
    }

    public Chain add(Chain chain) {
        setNext(chain);
        return getNext();
    }

    public abstract Pattern getPattern();

    public abstract void doJob(final HomeGroupBotFacade homeGroupBotFacade, final Long chatId, final String text, final User user, final ReplyKeyboardMarkup keyboardMarkup, final Long adminId);

    protected Date getDate(String dayString, String monthString, String yearString) {
        Date date;
        Integer day = new Integer(dayString);
        Integer month = new Integer(monthString) - 1;
        int year = yearString.length() == 4 ? new Integer(yearString) : new Integer(yearString) + 2000;

        Calendar calendar = new GregorianCalendar(year, month, day);
        date = calendar.getTime();
        return date;
    }

    protected String getStringOfDate(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.YEAR);
    }
}
