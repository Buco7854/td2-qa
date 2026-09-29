package minibuild;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
public class LineBasedPomParser implements IPomParser {
 public Project parse(ILineReader reader) throws IOException {
  String name = null, line;
  Set<Gav> deps = new HashSet<>();
  while ((line = reader.readLine()) != null) {
   line = line.trim();
   if (line.isEmpty()) continue;
   if (line.startsWith("project ")) name = line.substring(8);
   else if (line.startsWith("dependency ")) deps.add(Gav.parse(line.substring(11)));
  }
  if (name == null) throw new IllegalArgumentException("Fichier vide");
  return new Project(name, deps);
 }
}
