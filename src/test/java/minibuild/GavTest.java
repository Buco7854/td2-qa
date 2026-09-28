package minibuild;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import static org.junit.jupiter.api.Assertions.*;
@Tag("unit") class GavTest {
 @ParameterizedTest @CsvFileSource(resources="/gav-valides.csv")
 void parse(String text, String group, String artifact, String version) {
  Gav gav = Gav.parse(text);
  assertEquals(group, gav.group()); assertEquals(artifact, gav.artifact()); assertEquals(version, gav.version());
 }
 @Test void rejetteUnePartieVide() {
  assertThrows(IllegalArgumentException.class, () -> Gav.parse("org.acme::1.0.0"));
 }
}
