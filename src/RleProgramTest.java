import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class RleProgramTest {
    @Test

        public void stringToRle() {
        String input = "15f:64";
        byte [] res = { 15, 15, 6, 4 };
        assertArrayEquals(res, RleProgram.stringToRle(input));

        }


    }


