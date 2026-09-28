package minibuild;

import java.io.BufferedReader;
import java.io.StringReader;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Tag("unit")
class BufferedLineReaderTest {
    @Test
    void litLesLignesPuisSignaleLaFin() throws Exception {
        ILineReader reader = new BufferedLineReader(new BufferedReader(new StringReader("première\nseconde\n")));
        assertEquals("première", reader.readLine());
        assertEquals("seconde", reader.readLine());
        assertNull(reader.readLine());
    }
}
