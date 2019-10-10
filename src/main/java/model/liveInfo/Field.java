package model.liveInfo;

import java.util.HashSet;
import java.util.Set;

import static model.liveInfo.Section.SOCIAL_NETWORK;
import static model.liveInfo.Section.TIMETABLE;
import static model.liveInfo.Section.CONFERENCE;

public enum Field {
    CONF_DAYS("confDays", "Даты конференции", CONFERENCE),
    REGISTRATION("registration", "Регистрация", CONFERENCE),
    RESETTLEMENT("resettlement", "Расселение", CONFERENCE),

    INSTAGRAM("instagram", "Instagram", SOCIAL_NETWORK),
    VK("vk", "Vk", SOCIAL_NETWORK),
    TELEGRAM("telegram", "Telegram", SOCIAL_NETWORK),
    YOUTUBE("youtube", "Youtube", SOCIAL_NETWORK),

    SATURDAY_SERVICE("saturdayService", "Субботнее служение", TIMETABLE),
    FIRST_SERVICE("firstService", "Первое воскресное служение", TIMETABLE),
    SECOND_SERVICE("secondService", "Второе воскресное служение", TIMETABLE),
    PREPARETY("preparety", "Препати", TIMETABLE),
    YOUTH_SERVICE("youthService", "Молодёжное служение", TIMETABLE),
    AFTERPARTY("afterparty", "Автопати", TIMETABLE),
    MORNING_PRAYER("morningPrayer", "Утренняя молитва", TIMETABLE),
    NIGHT_PRAYER("nightPrayer", "Ночная молитва", TIMETABLE),
    HOME_GROUPS("homeGroups", "Домашние группы", TIMETABLE);

    private final String code;
    private final String name;
    private final Section section;

    Field(String code, String name, Section section) {
        this.code = code;
        this.name = name;
        this.section = section;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Section getSection() {
        return section;
    }

    public static Set<String> getAllCodes() {
        Set<String> result = new HashSet<String>();
        for (Field field : Field.values()) {
            result.add(field.getCode());
        }
        return result;
    }

    public static Field getByCode(String code) {
        if (code == null || code.isEmpty() || !getAllCodes().contains(code)) {
            return null;
        }
        for (Field field : Field.values()) {
            if (field.getCode().equals(code)) {
                return field;
            }
        }
        return null;
    }
}
