package minibuild;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
@Tag("unit") class GavTest {
 @Test void premierGroupe() {
  assertEquals("org.acme", Gav.parse("org.acme:lib-a:1.0.0").group());
 }
}
