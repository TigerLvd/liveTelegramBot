package utils;

import org.apache.commons.codec.binary.Base64;

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
        Integer day = new Integer(dayString);
        Integer month = new Integer(monthString) - 1;
        int year = yearString.length() == 4 ? new Integer(yearString) : new Integer(yearString) + 2000;

        Calendar calendar = new GregorianCalendar(year, month, day);
        date = calendar.getTime();
        return date;
    }

    public static String getStringOfDate(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.YEAR);
    }
}
