package minibuild;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.Set;

public class AllVersionsResolver implements IResolver {
    private final IRegistry registry;

    public AllVersionsResolver(IRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Set<Gav> resolve(Set<Gav> directDependencies) {
        Set<Gav> visited = new LinkedHashSet<>();
        Deque<Gav> pending = new ArrayDeque<>(directDependencies);
        while (!pending.isEmpty()) {
            Gav gav = pending.removeFirst();
            if (visited.add(gav)) {
                Artifact artifact = registry.lookup(gav)
                    .orElseThrow();
                pending.addAll(artifact.dependencies());
            }
        }
        return Set.copyOf(visited);
    }
}
