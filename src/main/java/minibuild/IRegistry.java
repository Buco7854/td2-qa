package minibuild;

import java.util.Optional;

public interface IRegistry {
    void publish(Artifact artifact) throws AlreadyPublishedException;
    Optional<Artifact> lookup(Gav gav);
}
