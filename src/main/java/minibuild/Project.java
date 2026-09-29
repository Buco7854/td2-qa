package minibuild;

import java.util.Set;

public record Project(String name, Set<Gav> dependencies) {
    public Project {
        dependencies = Set.copyOf(dependencies);
    }
}
