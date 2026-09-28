package minibuild;

import java.util.Set;

public record Artifact(Gav gav, Set<Gav> dependencies) {
    public Artifact {
        dependencies = Set.copyOf(dependencies);
    }
}
