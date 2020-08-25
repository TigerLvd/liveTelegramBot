package utils;

import model.homeGroups.db.HomeGroup;
import model.homeGroups.db.StatInfo;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {
    public static String base64Encode(String str) {
        if (null == str) {
            return null;
        }
        return Base64.encodeBase64String(str.getBytes());
    }

    public static String base64Decode(String str) {
        if (null == str) {
            return null;
        }

        return new String(Base64.decodeBase64(str));
    }

    public static boolean isField(Object object) {
        return !isEmpty(object);
    }

    public static boolean isEmpty(Object object) {
        if (null == object) {
            return true;
        } else if (object instanceof String) {
            return ((String) object).isEmpty();
        } else if (object instanceof Collection) {
            return ((Collection) object).isEmpty();
        } else if (object instanceof Map) {
            return ((Map) object).isEmpty();
        } else if (object.getClass().isArray()) {
            return Array.getLength(object) == 0;
        } else {
            return false;
        }
    }

    public static Date getDateOnly(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date addDay(Date date, int day) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    public static String getDateString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        return  dateFormat.format(date);
    }

    public static Date getDate(String dayString, String monthString, String yearString) {
        Date date;
        int day = Integer.parseInt(dayString);
        int month = Integer.parseInt(monthString) - 1;
        int year = yearString.length() == 4 ? Integer.parseInt(yearString) : Integer.parseInt(yearString) + 2000;

        Calendar calendar = new GregorianCalendar(year, month, day);
        date = calendar.getTime();
        return date;
    }

    public static String getStringOfDate(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.YEAR);
    }

    public static Date getStartYearDate() {
        Date firstDay = getFirstDayOfYear(new Date());
        if (getDayOfWeek(firstDay) > 2) {
            firstDay = addDay(firstDay, 7);
        }
        firstDay = getFirstDayOfWeek(firstDay);
        return firstDay;
    }

    public static Date getFirstDayOfYear(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Integer getDayOfWeek(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            dayOfWeek = 7;
        } else {
            dayOfWeek--;
        }
        return dayOfWeek;
    }

    public static Date getFirstDayOfWeek(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            dayOfWeek = 7;
        } else {
            dayOfWeek--;
        }
        calendar.add(Calendar.DAY_OF_MONTH, dayOfWeek * (-1) + 1); // получили понедельник
        return calendar.getTime();
    }

    /**
     * @param monday понедельник, от которого идёт отсчёт.
     * @return строка в формате: "<понедельник>-<воскресенье>"
     */
    public static String getMndToSunString(Date monday) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(monday);
        calendar.add(Calendar.DAY_OF_MONTH, 6);
        return getStringOfDate(monday) + "-" + getStringOfDate(calendar.getTime());
    }
}
