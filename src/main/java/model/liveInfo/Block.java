package model.liveInfo;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import static model.liveInfo.Section.*;

public enum Block {
    GENERAL_MENU("general_Menu", "Главное меню") {
        @Override
        public EnumSet<Section> getSections() {
            return EnumSet.of(TIMETABLE, SOCIAL_NETWORK, CONFERENCE);
        }
    };

    private final String code;

    private final String name;

    Block(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public EnumSet<Section> getSections() {
        return null;
    }

    public static Set<String> getAllCodes() {
        Set<String> result = new HashSet<String>();
        for (Block block : Block.values()) {
            result.add(block.getCode());
        }
        return result;
    }

    public static Block getByCode(String code) {
        if (code == null || code.isEmpty() || !getAllCodes().contains(code)) {
            return null;
        }
        for (Block block : Block.values()) {
            if (block.getCode().equals(code)) {
                return block;
            }
        }
        return null;
    }
}
