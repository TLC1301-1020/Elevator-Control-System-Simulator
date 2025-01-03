import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

class GenerateInputTest {

    //checks for format of the generated input and excepted number of lines
    @Test
    void main_GeneratesInputLines() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        //reference to the original object
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        //calls the main method in the class, generate input lines
        GenerateInput.main(new String[]{});

        System.setOut(originalOut);

        //byte array to string and splits into array of lines
        String[] lines = outContent.toString().split(System.lineSeparator());

        assertEquals(50, lines.length);

        for (String line : lines) {
            //each line has the format HH:MM:SS [TIME] [Original Floor] [Direction] [Destination Floor]
            assertTrue(line.matches("\\d{1,2}:\\d{1,2}:\\d{1,2} \\d{1,2} (Up|Down) \\d{1,2}"));
            System.out.println("Generated Line: " + line);
        }
    }
}
