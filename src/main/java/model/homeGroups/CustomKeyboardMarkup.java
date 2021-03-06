package model.homeGroups;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomKeyboardMarkup extends ReplyKeyboardMarkup {
    public static final Integer ROWS_SIZE = 2;

    public CustomKeyboardMarkup(String... keyNames) {
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        for (String keyName : keyNames) {
            if (row.size() == ROWS_SIZE) {
                keyboard.add(row);
                row = new KeyboardRow();
            }
            row.add(keyName);
        }
        if (row.size() != 0) {
            keyboard.add(row);
        }
        this.setKeyboard(keyboard);
    }

    public CustomKeyboardMarkup(Collection<String> keyNames) {
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        for (String keyName : keyNames) {
            if (row.size() == ROWS_SIZE) {
                keyboard.add(row);
                row = new KeyboardRow();
            }
            row.add(keyName);
        }
        if (row.size() != 0) {
            keyboard.add(row);
        }
        this.setKeyboard(keyboard);
    }
}
