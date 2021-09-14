import model.homegroups.CustomKeyboardMarkup;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CustomKeyboardTest {

    @Test
    public void zeroInput() {
        CustomKeyboardMarkup keyboard = new CustomKeyboardMarkup();
        assertEquals(0, keyboard.getKeyboard().size());
    }

    @Test
    public void oneInput() {
        CustomKeyboardMarkup keyboard = new CustomKeyboardMarkup("one");
        assertEquals(1, keyboard.getKeyboard().size());
    }

    @Test
    public void moreInput() {
        CustomKeyboardMarkup keyboard = new CustomKeyboardMarkup("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13");
        if (CustomKeyboardMarkup.ROWS_SIZE == 3) {
            assertEquals(5, keyboard.getKeyboard().size());
        }
        if (CustomKeyboardMarkup.ROWS_SIZE == 2) {
            assertEquals(7, keyboard.getKeyboard().size());
        }
    }
}