package minibuild;

public class MissingArtifactException extends RuntimeException {
    public MissingArtifactException(Gav gav) {
        super("Artefact absent du registre : " + gav);
    }
}
