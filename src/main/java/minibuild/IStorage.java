package minibuild;

import java.util.Optional;

public interface IStorage {
    void put(Gav gav, Artifact artifact);
    Optional<Artifact> get(Gav gav);
}
