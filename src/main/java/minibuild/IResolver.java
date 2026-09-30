package minibuild;

import java.util.Set;

public interface IResolver {
    Set<Gav> resolve(Set<Gav> directDependencies);
}
