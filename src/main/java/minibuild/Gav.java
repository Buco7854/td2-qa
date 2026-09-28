package minibuild;

public record Gav(String group, String artifact, String version) {
    public Gav {
        validate(group);
        validate(artifact);
        validate(version);
    }

    public static Gav parse(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Coordonnée absente");
        }
        String[] parts = text.split(":", -1);
        if (parts.length != 3) {
            throw new IllegalArgumentException("Coordonnée attendue : groupe:artefact:version");
        }
        return new Gav(parts[0], parts[1], parts[2]);
    }

    private static void validate(String part) {
        if (part == null || !part.matches("[A-Za-z0-9_.-]+")) {
            throw new IllegalArgumentException("Partie de coordonnée invalide : " + part);
        }
    }

    @Override
    public String toString() {
        return group + ":" + artifact + ":" + version;
    }
}
