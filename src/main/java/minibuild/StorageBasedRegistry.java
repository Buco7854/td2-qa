package minibuild;

import java.util.Optional;

public class StorageBasedRegistry implements IRegistry {
    private final IStorage storage;

    public StorageBasedRegistry(IStorage storage) {
        this.storage = storage;
    }

    @Override
    public void publish(Artifact artifact) {
        if (storage.get(artifact.gav()).isPresent()) {
            throw new AlreadyPublishedException(artifact.gav());
        }
        storage.put(artifact.gav(), artifact);
    }

    @Override
    public Optional<Artifact> lookup(Gav gav) {
        return storage.get(gav);
    }
}
