package minibuild;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("unit")
class GavTest {
    @ParameterizedTest
    @CsvSource({
        "org.acme:lib-a:1.0.0, org.acme, lib-a, 1.0.0",
        "org.other:lib-c:3.0.0, org.other, lib-c, 3.0.0"
    })
    void parseDesValeursDansLeCode(String text, String group, String artifact, String version) {
        assertGav(text, group, artifact, version);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/gav-valides.csv")
    void parseDesValeursDansUnFichier(String text, String group, String artifact, String version) {
        assertGav(text, group, artifact, version);
    }

    private void assertGav(String text, String group, String artifact, String version) {
        Gav gav = Gav.parse(text);
        assertThat(gav.group(), is(group));
        assertThat(gav.artifact(), is(artifact));
        assertThat(gav.version(), is(version));
        assertThat(gav.toString(), is(text));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
        "org.acme", "org.acme:lib-a", "org.acme:lib-a:1:extra",
        ":lib-a:1.0", "org.acme::1.0", "org.acme:lib-a:",
        "org acme:lib-a:1.0", "org.acme:lib a:1.0", "org.acme:lib-a:1 0"
    })
    void rejetteUneCoordonneeInvalide(String text) {
        assertThrows(IllegalArgumentException.class, () -> Gav.parse(text));
    }

    @Test
    void rejetteUnComposantNullMemeParLeConstructeur() {
        assertThrows(IllegalArgumentException.class, () -> new Gav(null, "lib-a", "1.0.0"));
    }

    @Test
    void conserveLaCasseDuGroupe() {
        assertThat(Gav.parse("Org.Acme:lib-a:1.0.0").group(), is("Org.Acme"));
    }
}
