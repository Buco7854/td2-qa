package minibuild;

import java.io.IOException;
import java.util.Set;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("unit")
class LineBasedPomParserTest {
    private final IPomParser parser = new LineBasedPomParser();
    private final Gav a = Gav.parse("org.acme:lib-a:1.0.0");
    private final Gav b = Gav.parse("org.acme:lib-b:2.1.0");

    private ILineReader lines(String... values) throws IOException {
        ILineReader reader = mock(ILineReader.class);
        when(reader.readLine()).thenReturn(values[0], java.util.Arrays.copyOfRange(values, 1, values.length));
        return reader;
    }

    @Test
    void fichierVide() throws Exception {
        // [ILineReader | readLine ↦ ⊥]s ⊢ parse(reader) ⇒ ↯ IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> parser.parse(lines((String) null)));
    }

    @Test
    void projetSeul() throws Exception {
        // [ILineReader | readLine ↦ ⟨"project mon-app", ⊥⟩]s ⊢ parse(reader) ⇒ Project("mon-app", ∅)
        assertEquals(new Project("mon-app", Set.of()), parser.parse(lines("project mon-app", null)));
    }

    @Test
    void uneDependance() throws Exception {
        // [ILineReader | readLine ↦ ⟨"project mon-app", "dependency a", ⊥⟩]s ⊢ parse(reader) ⇒ Project("mon-app", {a})
        assertEquals(new Project("mon-app", Set.of(a)),
            parser.parse(lines("project mon-app", "dependency " + a, null)));
    }

    @Test
    void plusieursDependancesEtLignesVides() throws Exception {
        // [ILineReader | lignes du projet, a, ligne vide, b, ⊥]s ⊢ parse(reader) ⇒ Project("mon-app", {a,b})
        assertEquals(new Project("mon-app", Set.of(a, b)),
            parser.parse(lines("", "project mon-app", "dependency " + a, " ", "dependency " + b, null)));
    }
}
