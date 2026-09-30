package minibuild;

import java.io.IOException;
import java.util.Set;

public class BuildTool {
    private final IPomParser parser;
    private final IResolver resolver;

    public BuildTool(IPomParser parser, IResolver resolver) {
        this.parser = parser;
        this.resolver = resolver;
    }

    public Set<Gav> build(ILineReader reader) throws IOException {
        return resolver.resolve(parser.parse(reader).dependencies());
    }
}
