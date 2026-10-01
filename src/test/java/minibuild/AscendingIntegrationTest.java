package minibuild;

import java.util.Set;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("integration")
class AscendingIntegrationTest {
    @Test
    void etape1_piloteDuRegistreEtDuStockage() {
        // Le test pilote IRegistry ; interface IRegistry → IStorage réelle.
        Gav a = Gav.parse("org.acme:lib-a:1.0.0");
        Artifact artifact = new Artifact(a, Set.of());
        IRegistry registry = new StorageBasedRegistry(new InMemoryStorage());
        registry.publish(artifact);
        assertEquals(artifact, registry.lookup(a).orElseThrow());
        assertThrows(AlreadyPublishedException.class, () -> registry.publish(artifact));
    }
}
