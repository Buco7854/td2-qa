package minibuild;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
@Tag("unit") class GavTest {
 @Test void premierGroupe() {
  assertEquals("org.acme", Gav.parse("org.acme:lib-a:1.0.0").group());
 }
 @Test void deuxiemeExemple() {
  Gav gav = Gav.parse("org.other:lib-c:3.0.0");
  assertEquals("org.other", gav.group());
  assertEquals("lib-c", gav.artifact());
  assertEquals("3.0.0", gav.version());
 }
}
