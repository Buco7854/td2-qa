package minibuild;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryStorage implements IStorage {
    private final Map<Gav, Artifact> artifacts = new HashMap<>();

    @Override
    public void put(Gav gav, Artifact artifact) {
        artifacts.put(gav, artifact);
    }

    @Override
    public Optional<Artifact> get(Gav gav) {
        return Optional.ofNullable(artifacts.get(gav));
    }
}
