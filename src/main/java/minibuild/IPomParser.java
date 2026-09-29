package minibuild;

import java.io.IOException;

public interface IPomParser {
    Project parse(ILineReader reader) throws IOException;
}
