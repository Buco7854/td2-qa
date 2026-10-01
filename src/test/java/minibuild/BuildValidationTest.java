package minibuild;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("validation")
class BuildValidationTest {
    private final Gav a = Gav.parse("org.acme:lib-a:1.0.0");
    private final Gav b = Gav.parse("org.acme:lib-b:2.1.0");
    private final Gav c = Gav.parse("org.other:lib-c:3.0.0");

    private IRegistry registry() {
        IRegistry registry = new StorageBasedRegistry(new InMemoryStorage());
        registry.publish(new Artifact(a, Set.of(c)));
        registry.publish(new Artifact(b, Set.of()));
        registry.publish(new Artifact(c, Set.of()));
        return registry;
    }

    private Set<Gav> build(String resource, IRegistry registry) throws Exception {
        Path path = Path.of(getClass().getResource(resource).toURI());
        try (BufferedReader file = Files.newBufferedReader(path)) {
            return new BuildTool(new LineBasedPomParser(), new AllVersionsResolver(registry))
                .build(new BufferedLineReader(file));
        }
    }

    @Test
    void resoutUnVraiFichierEtSesDependancesTransitives() throws Exception {
        assertThat(build("/build-valid.txt", registry()), containsInAnyOrder(a, b, c));
    }

    @Test
    void signaleUneDependanceAbsente() {
        assertThrows(MissingArtifactException.class, () -> build("/build-missing.txt", registry()));
    }

    @Test
    void signaleUneSyntaxeInvalide() {
        assertThrows(IllegalArgumentException.class, () -> build("/build-invalid.txt", registry()));
    }
}
