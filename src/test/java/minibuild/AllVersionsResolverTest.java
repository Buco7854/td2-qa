package minibuild;

import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("unit")
class AllVersionsResolverTest {
    private final Gav a = Gav.parse("org.acme:lib-a:1.0.0");
    private final Gav b = Gav.parse("org.acme:lib-b:2.1.0");
    private final Gav c = Gav.parse("org.other:lib-c:3.0.0");

    private IRegistry chain(boolean cycle) {
        IRegistry registry = mock(IRegistry.class);
        when(registry.lookup(a)).thenReturn(Optional.of(new Artifact(a, Set.of(b))));
        when(registry.lookup(b)).thenReturn(Optional.of(new Artifact(b, Set.of(c))));
        when(registry.lookup(c)).thenReturn(Optional.of(new Artifact(c, cycle ? Set.of(a) : Set.of())));
        return registry;
    }

    @Test
    void fermetureTransitiveAvecJUnitEtHamcrest() {
        // [IRegistry | lookup(a)↦A(b), lookup(b)↦B(c), lookup(c)↦C(∅)]s ⊢ resolve({a}) ⇒ {a,b,c}
        Set<Gav> result = new AllVersionsResolver(chain(false)).resolve(Set.of(a));
        assertEquals(Set.of(a, b, c), result);
        assertThat(result, containsInAnyOrder(a, b, c));
    }

    @Test
    void interrogeChaqueCoordonneeUneSeuleFois() {
        // [IRegistry | chaîne a→b→c, appels enregistrés]sp ⊢ resolve({a}) ⇒ lookup(a,b,c) ×1 chacun
        IRegistry registry = chain(false);
        new AllVersionsResolver(registry).resolve(Set.of(a));
        verify(registry, times(1)).lookup(a);
        verify(registry, times(1)).lookup(b);
        verify(registry, times(1)).lookup(c);
    }

    @Test
    void cycleSansBoucleInfinie() {
        // [IRegistry | chaîne a→b→c→a, appels enregistrés]sp ⊢ resolve({a}) ⇒ {a,b,c}, lookup ×1 chacun
        IRegistry registry = chain(true);
        assertEquals(Set.of(a, b, c), new AllVersionsResolver(registry).resolve(Set.of(a)));
        verify(registry, times(1)).lookup(a);
        verify(registry, times(1)).lookup(b);
        verify(registry, times(1)).lookup(c);
    }

    @Test
    void dependanceAbsente() {
        IRegistry registry = mock(IRegistry.class);
        when(registry.lookup(a)).thenReturn(Optional.empty());
        assertThrows(MissingArtifactException.class,
            () -> new AllVersionsResolver(registry).resolve(Set.of(a)));
    }
}
