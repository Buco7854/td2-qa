package minibuild;

import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("unit")
class StorageBasedRegistryTest {
    private final Gav a = Gav.parse("org.acme:lib-a:1.0.0");
    private final Artifact artifact = new Artifact(a, Set.of());

    // Bouchon manuel : réponse fixée à l'avance, avec trace des écritures.
    private static class StorageStub implements IStorage {
        private final Optional<Artifact> answer;
        private int writes;
        private Artifact written;

        StorageStub(Optional<Artifact> answer) {
            this.answer = answer;
        }

        @Override
        public void put(Gav gav, Artifact artifact) {
            writes++;
            written = artifact;
        }

        @Override
        public Optional<Artifact> get(Gav gav) {
            return answer;
        }
    }

    @Test
    void publieAvecBouchonManuel() {
        // [IStorage | get(a) ↦ ∅, put(a,A) enregistré]sp ⊢ publish(A) ⇒ put ×1
        StorageStub storage = new StorageStub(Optional.empty());
        IRegistry registry = new StorageBasedRegistry(storage);
        registry.publish(artifact);
        assertEquals(1, storage.writes);
        assertEquals(artifact, storage.written);
    }

    @Test
    void rechercheAvecBouchonManuel() {
        // [IStorage | get(a) ↦ A]s ⊢ lookup(a) ⇒ A
        StorageStub storage = new StorageStub(Optional.of(artifact));
        assertEquals(Optional.of(artifact), new StorageBasedRegistry(storage).lookup(a));
    }

    @Test
    void refuseUneRepublicationAvecDoublureManuelle() {
        // [IStorage | get(a) ↦ A, put enregistré]sp ⊢ publish(A) ⇒ ↯ AlreadyPublishedException et put ×0
        StorageStub storage = new StorageStub(Optional.of(artifact));
        IRegistry registry = new StorageBasedRegistry(storage);
        assertThrows(AlreadyPublishedException.class, () -> registry.publish(artifact));
        assertEquals(0, storage.writes);
    }

    @Test
    void publieEtRechercheAvecMockito() {
        // [IStorage | get(a) ↦ ∅ puis A]s, put(a,A) observé ⊢ publish(A); lookup(a) ⇒ A
        IStorage storage = mock(IStorage.class);
        when(storage.get(a)).thenReturn(Optional.empty()).thenReturn(Optional.of(artifact));
        IRegistry registry = new StorageBasedRegistry(storage);
        registry.publish(artifact);
        assertEquals(Optional.of(artifact), registry.lookup(a));
        verify(storage).put(a, artifact);
    }

    @Test
    void refuseUneRepublicationAvecMockito() {
        // [IStorage | get(a) ↦ A]s, put(a,A) observé ⊢ publish(A) ⇒ ↯ AlreadyPublishedException et put ×0
        IStorage storage = mock(IStorage.class);
        when(storage.get(a)).thenReturn(Optional.of(artifact));
        IRegistry registry = new StorageBasedRegistry(storage);
        assertThrows(AlreadyPublishedException.class, () -> registry.publish(artifact));
        verify(storage, never()).put(a, artifact);
    }

    @Test
    void rechercheAbsente() {
        IStorage storage = mock(IStorage.class);
        when(storage.get(a)).thenReturn(Optional.empty());
        assertTrue(new StorageBasedRegistry(storage).lookup(a).isEmpty());
    }
}
