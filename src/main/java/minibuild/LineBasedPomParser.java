package minibuild;
import java.io.IOException;
public class LineBasedPomParser implements IPomParser {
 public Project parse(ILineReader reader) throws IOException {
  throw new IllegalArgumentException("Fichier vide");
 }
}
