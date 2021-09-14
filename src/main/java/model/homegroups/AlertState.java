package model.homegroups;

import java.util.HashSet;
import java.util.Set;

public enum AlertState {
    ON("notice_on", "включить уведомления"),
    OFF	("notice_off", "отключить уведомления");

    private final String code;
    private final String title;

    AlertState(String code, String title) {
        this.code = code;
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public static Set<String> getAllCodes() {
        Set<String> res = new HashSet<>();
        for (AlertState value : values()) {
            res.add(value.getCode());
        }
        return res;
    }

    public static Set<String> getAllTitles() {
        Set<String> res = new HashSet<>();
        for (AlertState value : values()) {
            res.add(value.getTitle());
        }
        return res;
    }
}
