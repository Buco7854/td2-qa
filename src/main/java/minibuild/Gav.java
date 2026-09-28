package minibuild;
public record Gav(String group, String artifact, String version) {
 public static Gav parse(String text) {
  String[] parts = text.split(":", -1);
  return new Gav(parts[0], parts[1], parts[2]);
 }
 @Override public String toString() { return group + ":" + artifact + ":" + version; }
}
