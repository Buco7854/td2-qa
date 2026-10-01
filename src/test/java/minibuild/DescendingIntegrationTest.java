package minibuild;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("integration")
class DescendingIntegrationTest {
    private final Gav a = Gav.parse("org.acme:lib-a:1.0.0");
    private final Gav b = Gav.parse("org.acme:lib-b:2.1.0");
    private final IPomParser parser = new LineBasedPomParser();
    private final String file = "project mon-app\ndependency " + a + "\n";

    private ILineReader realReader() {
        return new BufferedLineReader(new BufferedReader(new StringReader(file)));
    }

    @Test
    void etape1_buildToolEtParser() throws Exception {
        // Interface BuildTool → IPomParser ; ILineReader et IResolver sont bouchonnés.
        ILineReader reader = mock(ILineReader.class);
        when(reader.readLine()).thenReturn("project mon-app", "dependency " + a, null);
        IResolver resolver = mock(IResolver.class);
        when(resolver.resolve(Set.of(a))).thenReturn(Set.of(a));
        assertEquals(Set.of(a), new BuildTool(parser, resolver).build(reader));
        verify(resolver).resolve(Set.of(a));
    }

    @Test
    void etape2_ajoutDuLecteur() throws Exception {
        // Interface IPomParser → ILineReader ; seul IResolver reste bouchonné.
        IResolver resolver = mock(IResolver.class);
        when(resolver.resolve(Set.of(a))).thenReturn(Set.of(a));
        assertEquals(Set.of(a), new BuildTool(parser, resolver).build(realReader()));
    }
}
