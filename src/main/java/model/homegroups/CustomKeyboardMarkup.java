package model.homegroups;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CustomKeyboardMarkup extends ReplyKeyboardMarkup {
    public static final Long ROWS_SIZE = 2L;

    public CustomKeyboardMarkup(String... keyNames) {
        this(Arrays.stream(keyNames).collect(Collectors.toList()));
    }

    public CustomKeyboardMarkup(List<String> keyNames) {
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
