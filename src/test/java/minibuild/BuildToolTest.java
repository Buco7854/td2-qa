package minibuild;

import java.util.Set;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("unit")
class BuildToolTest {
    @Test
    void transmetLesDependancesDuProjetAuResolveur() throws Exception {
        // [IPomParser | parse(r)↦Project(p,{a})]s, [IResolver | resolve({a})↦{a,b}]s/sp
        // ⊢ build(r) ⇒ {a,b} et resolve({a}) ×1
        Gav a = Gav.parse("org.acme:lib-a:1.0.0");
        Gav b = Gav.parse("org.acme:lib-b:2.1.0");
        ILineReader reader = mock(ILineReader.class);
        IPomParser parser = mock(IPomParser.class);
        IResolver resolver = mock(IResolver.class);
        when(parser.parse(reader)).thenReturn(new Project("mon-app", Set.of(a)));
        when(resolver.resolve(Set.of(a))).thenReturn(Set.of(a, b));
        assertEquals(Set.of(a, b), new BuildTool(parser, resolver).build(reader));
        verify(resolver).resolve(Set.of(a));
    }
}
