package minibuild;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

public class LineBasedPomParser implements IPomParser {
    @Override
    public Project parse(ILineReader reader) throws IOException {
        String name = null;
        Set<Gav> dependencies = new LinkedHashSet<>();
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            if (name == null && line.matches("project [^\\s]+")) {
                name = line.substring("project ".length());
            } else if (name != null && line.startsWith("dependency ")) {
                dependencies.add(Gav.parse(line.substring("dependency ".length())));
            } else {
                throw new IllegalArgumentException("Ligne de build invalide : " + line);
            }
        }
        if (name == null) {
            throw new IllegalArgumentException("Le fichier de build doit déclarer un projet");
        }
        return new Project(name, dependencies);
    }
}
