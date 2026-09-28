package minibuild;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
@Tag("unit") class GavTest {
 @ParameterizedTest @CsvSource({
  "org.acme:lib-a:1.0.0,org.acme,lib-a,1.0.0",
  "org.other:lib-c:3.0.0,org.other,lib-c,3.0.0"
 }) void parse(String text, String group, String artifact, String version) {
  Gav gav = Gav.parse(text);
  assertEquals(group, gav.group());
  assertEquals(artifact, gav.artifact());
  assertEquals(version, gav.version());
 }
}
