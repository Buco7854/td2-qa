package minibuild;

import java.io.BufferedReader;
import java.io.IOException;

public class BufferedLineReader implements ILineReader {
    private final BufferedReader reader;

    public BufferedLineReader(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public String readLine() throws IOException {
        return reader.readLine();
    }
}
