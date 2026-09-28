package minibuild;

import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("unit")
class InMemoryStorageTest {
    private IStorage storage;
    private final Gav a = Gav.parse("org.acme:lib-a:1.0.0");

    @BeforeEach
    void init() {
        storage = new InMemoryStorage();
    }

    @Test
    void absentAvantInsertion() {
        assertTrue(storage.get(a).isEmpty());
    }

    @Test
    void retrouveCeQuiEstInsere() {
        Artifact artifact = new Artifact(a, Set.of());
        storage.put(a, artifact);
        assertEquals(artifact, storage.get(a).orElseThrow());
    }

    @Test
    void remplaceLaValeurPourUneMemeCle() {
        storage.put(a, new Artifact(a, Set.of()));
        Artifact replacement = new Artifact(a, Set.of(Gav.parse("org.other:lib-c:3.0.0")));
        storage.put(a, replacement);
        assertEquals(replacement, storage.get(a).orElseThrow());
    }
}
