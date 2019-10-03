import model.CustomKeyboardMarkup;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CustomKeyboardTest {

    @Test
    public void zeroInput() {
        CustomKeyboardMarkup keyboard = new CustomKeyboardMarkup();
    }

    @Test
    public void oneInput() {
        CustomKeyboardMarkup keyboard = new CustomKeyboardMarkup("one");
        assertEquals(1, keyboard.getKeyboard().size());
    }

    @Test
    public void moreInput() {
        CustomKeyboardMarkup keyboard = new CustomKeyboardMarkup("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13");
        assertEquals(5, keyboard.getKeyboard().size());
    }

    @Ignore("Message for ignored test")
    @Test
    public void ignoredTest() {
        System.out.println("will not print it");
    }
}