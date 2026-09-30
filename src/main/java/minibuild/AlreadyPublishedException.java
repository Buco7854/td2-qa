package minibuild;

public class AlreadyPublishedException extends RuntimeException {
    public AlreadyPublishedException(Gav gav) {
        super("Déjà publié : " + gav);
    }
}
