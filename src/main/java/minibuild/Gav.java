package minibuild;
public record Gav(String group, String artifact, String version) {
 public static Gav parse(String text) { return new Gav("org.acme", null, null); }
}
