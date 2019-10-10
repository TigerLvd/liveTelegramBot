package model.liveInfo;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import static model.liveInfo.Block.GENERAL_MENU;
import static model.liveInfo.Field.*;

public enum Section {

    TIMETABLE("timetable", "Расписание", GENERAL_MENU) {
        @Override
        public EnumSet<Field> getFields() {
            return EnumSet.of(SATURDAY_SERVICE, FIRST_SERVICE, SECOND_SERVICE,
                    PREPARETY, YOUTH_SERVICE, AFTERPARTY,
                    MORNING_PRAYER, NIGHT_PRAYER, HOME_GROUPS);
        }
    },
    SOCIAL_NETWORK("socialNetwork", "Соц.сети", GENERAL_MENU) {
        @Override
        public EnumSet<Field> getFields() {
            return EnumSet.of(INSTAGRAM, VK, TELEGRAM, YOUTUBE);
        }
    },
    CONFERENCE("conference", "Зимняя конференция live", GENERAL_MENU) {
        @Override
        public EnumSet<Field> getFields() {
            return EnumSet.of(CONF_DAYS, REGISTRATION, RESETTLEMENT);
        }
    };

    private final String code;

    private final String name;

    private final Block block;

    Section(String code, String name, Block block) {
        this.code = code;
        this.name = name;
        this.block = block;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Block getBlock() {
        return block;
    }

    public EnumSet<Field> getFields() {
        return null;
    }

    public static Set<String> getAllCodes() {
        Set<String> result = new HashSet<String>();
        for (Section section : Section.values()) {
            result.add(section.getCode());
        }
        return result;
    }

    public static Section getByCode(String code) {
        if (code == null || code.isEmpty() || !getAllCodes().contains(code)) {
            return null;
        }
        for (Section section : Section.values()) {
            if (section.getCode().equals(code)) {
                return section;
            }
        }
        return null;
    }
}
