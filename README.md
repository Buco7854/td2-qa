# minibuild (TD2)

Projet Java 17+ construit avec Gradle. Le fichier de build commence par `project nom`, puis contient une ligne `dependency group:artifact:version` par dépendance. Les lignes vides sont ignorées.

```bash
./gradlew test jacocoTestReport
./gradlew test jacocoTestReport -PtestLevel=unit
./gradlew test jacocoTestReport -PtestLevel=integration
./gradlew test jacocoTestReport -PtestLevel=validation
```

Rapport de couverture : `build/reports/jacoco/test/html/index.html`.

Les réponses aux questions sont dans [REPONSES.md](REPONSES.md). La liste des tâches est dans [TASKS.md](TASKS.md).
