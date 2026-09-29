package minibuild;
import java.io.IOException;
import java.util.Set;
public class LineBasedPomParser implements IPomParser {
 public Project parse(ILineReader reader) throws IOException {
  String line = reader.readLine();
  if (line == null) throw new IllegalArgumentException("Fichier vide");
  String next = reader.readLine();
  if (next == null) return new Project(line.substring("project ".length()), Set.of());
  return new Project(line.substring("project ".length()),
      Set.of(Gav.parse(next.substring("dependency ".length()))));
 }
}
