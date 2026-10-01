# Réponses au TD2

Les classes, les tests et les fichiers d'essai sont dans `src/`. Les descriptions des tests de la partie 3 figurent juste avant les tests concernés.

## Partie 2 : mise en place et TDD

**1.** Le projet Gradle utilise le plugin `java`, JaCoCo, JUnit 5, Hamcrest et Mockito. Le wrapper permet de lancer `./gradlew test` sans installer Gradle. Le dépôt est versionné avec Git.

**2.** JUnit lance les tests et fournit les assertions. Hamcrest fournit des matchers pour écrire les vérifications. Mockito crée des doublures et permet de vérifier leurs appels. JaCoCo mesure la couverture : c'est un outil d'analyse, pas une bibliothèque de test.

**3.** La liste des tâches est dans `TASKS.md`. J'ai ajouté en cours de route les cas de coordonnées invalides, les cycles, les versions multiples et les dépendances absentes.

**4.** Le premier cas de `GavTest` est `org.acme:lib-a:1.0.0`, avec `org.acme` attendu pour le groupe. Avant l'existence de `Gav.parse`, ce test ne peut pas compiler : c'est bien un état *red*.

**5.** Avec un faux `parse` qui renverrait toujours un `Gav` dont le groupe est `org.acme`, le premier test passerait. `org.acme` apparaîtrait alors dans le test **et** en dur dans le code : le programme mémoriserait l'exemple au lieu de traiter n'importe quelle entrée.

**6.** Le second cas, `org.other:lib-c:3.0.0`, vérifie les trois champs. `Gav.parse` découpe maintenant la chaîne en trois parties et valide chacune.

**7.** Les deux chaînes sont dans la même classe d'équivalence, celle des coordonnées valides. La triangulation ne cherche pas une nouvelle classe d'entrée. Elle empêche simplement une implémentation constante de satisfaire le test.

**8.** `GavTest` contient une version `@CsvSource` et une version `@CsvFileSource`, alimentée par `gav-valides.csv` dans les ressources de test.

**9.** J'ai distingué les entrées nulles ou vides, le mauvais nombre de parties (deux ou quatre), une partie vide et les caractères interdits comme l'espace. Chaque classe a un cas dans le test paramétré `rejetteUneCoordonneeInvalide`. Le constructeur refuse aussi une partie nulle.

**10.** `InMemoryStorageTest` utilise `@BeforeEach` pour repartir d'un stockage vide. `Optional.empty()` rend l'absence explicite dans la signature. Avec `null`, on risque d'oublier de traiter ce cas. Une exception serait excessive pour une simple recherche infructueuse.

**11.** Le lecteur est testé avec `StringReader`. Le TDD m'a peu aidé ici : `BufferedLineReader` ne fait que déléguer à `BufferedReader`. Le test vérifie surtout que les lignes et la fin du flux sont bien transmises.

**12.** Les assertions finales de `GavTest` utilisent Hamcrest. J'ai forcé temporairement `FAUX` comme valeur attendue. JUnit affiche `expected: <FAUX> but was: <org.acme>`. Hamcrest affiche `Expected: is "FAUX"` puis `but: was "org.acme"`. Les deux sont utiles, mais Hamcrest devient plus lisible pour des conditions composées.

## Partie 3 : tests unitaires

**13.** Un bouchon ne fait que répondre aux appels. `verify` vérifie en plus *quels appels* ont eu lieu et combien de fois. Un test d'état examine le résultat observable. Un test d'interaction examine les appels faits à la doublure.

**14.** Les cinq cas demandés sont dans `LineBasedPomParserTest` : fichier vide, projet seul, une dépendance, plusieurs dépendances avec lignes vides et entrées mal formées. J'ai choisi de lever `IllegalArgumentException` pour un fichier vide ou invalide. Chaque test a sa description avant le code.

**15.** Le lecteur simulé ne peut pas révéler un problème d'ouverture de fichier, de chemin, d'encodage, de lecture réelle ou de fermeture du flux. Ces points sont exercés plus tard par le test de validation.

**16.** Une doublure manuelle de `IStorage` puis une doublure Mockito testent `publish` et `lookup`. La première demande une petite classe et un compteur. Mockito évite ce code, mais le comportement est un peu moins évident au premier regard. Le test de republication vérifie **les deux** : l'exception (état observable) et l'absence de `put` (interaction).

**17.** Avec `a → b → c`, `AllVersionsResolverTest` vérifie `{a,b,c}` avec `assertEquals`, puis `containsInAnyOrder`. J'ai volontairement retiré `c` des valeurs attendues. JUnit compare l'ensemble attendu `{a,b}` à l'ensemble obtenu `{a,b,c}`. Hamcrest signale précisément `not matched: <org.other:lib-c:3.0.0>`.

**18.** Le second test vérifie `lookup(a)`, `lookup(b)` et `lookup(c)` une seule fois chacun. Il capture le fait qu'une coordonnée déjà rencontrée n'est pas recherchée de nouveau, ce que le seul ensemble final ne montre pas.

**19.** Le cycle `c → a` est ajouté à la task list et testé. Sans mémoire des coordonnées déjà vues, la boucle de résolution ne termine pas. Le résolveur ajoute donc chaque coordonnée à `visited` **avant** d'en charger les dépendances. Le test d'interaction montre plus vite qu'une même recherche se répète. Avec le test d'état, on attend surtout que le test expire.

**20.** On pourrait rechercher deux fois un artefact, par exemple une fois pour vérifier son existence et une fois pour charger ses dépendances : le résultat resterait identique, mais `verify(..., times(1))` échouerait. Je réserve donc `verify` aux échanges qui font partie d'une propriété utile, ici l'absence de recherches répétées.

**21.** `BuildToolTest` remplace le parseur et le résolveur. Sa description est : `[IPomParser | parse(r) ↦ Project(p,{a})]s, [IResolver | resolve({a}) ↦ {a,b}]s/sp ⊢ build(r) ⇒ {a,b}, resolve({a}) ×1`. Le test vérifie surtout la transmission exacte de `{a}`.

## Partie 4 : tests d'intégration

**22.** Graphe d'appel :

| Composant | Appelant | Appelé(s) |
|---|---|---|
| `BuildTool` | aucun | `LineBasedPomParser`, `AllVersionsResolver` |
| `LineBasedPomParser` | `BuildTool` | `BufferedLineReader` |
| `AllVersionsResolver` | `BuildTool` | `StorageBasedRegistry` |
| `StorageBasedRegistry` | `AllVersionsResolver` | `InMemoryStorage` |
| `InMemoryStorage` | `StorageBasedRegistry` | aucun |
| `BufferedLineReader` | `LineBasedPomParser` | aucun |

**23.** Une arête compte pour une unité :

| Composant | Hauteur | Profondeur |
|---|---:|---:|
| `BuildTool` | 3 | 0 |
| `LineBasedPomParser` | 1 | 1 |
| `AllVersionsResolver` | 2 | 1 |
| `StorageBasedRegistry` | 1 | 2 |
| `InMemoryStorage` | 0 | 3 |
| `BufferedLineReader` | 0 | 2 |

La branche du registre est plus longue que celle du lecteur. Pour un composant situé sur une même branche complète, hauteur + profondeur donne la longueur de cette branche.

**24.** Ordre descendant utilisé :

| Étape | Ajout | Interface testée | Bouchon(s) | Pilote |
|---|---|---|---|---|
| 1 | `BuildTool`, parseur | `BuildTool` → parseur | lecteur, résolveur | appel à `BuildTool` |
| 2 | lecteur | parseur → lecteur | résolveur | appel à `BuildTool` |
| 3 | résolveur | `BuildTool` → résolveur | registre | appel à `BuildTool` |
| 4 | registre | résolveur → registre | stockage | appel à `BuildTool` |
| 5 | stockage | registre → stockage | aucun | appel à `BuildTool` |

**25.** À l'étape 1, le parseur est réel : le résultat transmis au résolveur vient des lignes lues. Au test unitaire de `BuildTool`, le parseur était lui aussi simulé, donc l'interface `BuildTool`–parseur n'était pas éprouvée.

**26.** Les cinq étapes sont cinq tests distincts dans `DescendingIntegrationTest`. Les tests des étapes précédentes restent en place. À l'étape 5, les six composants sont réels.

**27.** Dès l'étape 1, une mauvaise interprétation de `Project.dependencies()` entre `BuildTool` et le parseur serait visible. En contrepartie, il faut préparer des réponses plausibles pour le lecteur et le résolveur, puis pour le registre et le stockage à mesure que l'on descend.

**28.** Ordre ascendant possible :

| Étape | Ajout | Interface testée | Bouchon(s) | Pilote |
|---|---|---|---|---|
| 1 | stockage, registre | registre → stockage | aucun | appel à `IRegistry` |
| 2 | résolveur | résolveur → registre | aucun | appel à `IResolver` |
| 3 | lecteur, parseur | parseur → lecteur | aucun | appel à `IPomParser` |
| 4 | `BuildTool` | `BuildTool` → parseur et résolveur | aucun | appel à `BuildTool` |

**29.** `AscendingIntegrationTest` implémente l'étape 1. Le pilote publie un artefact, le retrouve, puis tente de le republier.

**30.** Dans ces deux ordres précis :

| Critère | Descendante | Ascendante |
|---|---|---|
| Pilotes | un point d'entrée réutilisé | quatre points d'entrée successifs |
| Bouchons | quatre interfaces simulées, cinq usages au total | aucun |
| Structure de haut niveau | testée dès l'étape 1 | testée à l'étape 4 |
| Interfaces de bas niveau | les dernières | les premières |

Je choisirais ici l'ascendante si je voulais réduire la préparation des bouchons : les deux branches du bas sont assez simples à piloter. La descendante reste intéressante pour découvrir tôt une erreur de câblage autour de `BuildTool`.

**31.** Le *big bang* n'utilise aucun bouchon et un pilote suffit à l'entrée du système. S'il échoue, les cinq interfaces sont impliquées d'un coup : il devient beaucoup plus difficile de situer le défaut.

## Partie 5 : tests de validation

**32.** `BuildValidationTest` lit `build-valid.txt` sur disque avec un vrai `BufferedLineReader`, pré-alimente le registre réel et attend `{a,b,c}` avec Hamcrest. `c` n'est que transitive.

**33.** Le dernier test d'intégration utilise déjà tous les composants réels, mais ses données viennent d'un `StringReader` préparé par le test et ciblent le dernier raccord registre–stockage. La validation part d'un fichier de ressources réel et vérifie un scénario métier complet depuis l'entrée externe.

**34.** J'ai choisi `MissingArtifactException` pour une dépendance absente et `IllegalArgumentException` pour une syntaxe invalide. Le sujet demande de traiter ces situations, mais ne fixe pas la réaction exacte du système : ces exceptions sont donc une décision explicitée par les tests, pas une règle imposée par l'énoncé.

**35.** Un échec de validation dit que le résultat final est faux, sans désigner sa cause. Un échec unitaire pointe beaucoup mieux vers une classe. Un échec d'intégration réduit le problème à quelques interfaces. Les trois niveaux se complètent.

**36.** Une équipe indépendante pour la validation risque moins de reprendre sans recul les mêmes hypothèses que les développeurs et peut raisonner en termes d'usage. Les tests unitaires sont naturellement proches de la personne qui écrit chaque composant. L'intégration se fait par l'équipe qui assemble les composants. Ici, écrire seul les trois niveaux fait gagner du temps, mais augmente le risque de reproduire la même erreur d'interprétation partout.

## Partie 6 : couverture

**37.** Avant la mesure, j'attendais une couverture des instructions proche de 100 %, puisque les comportements ont presque tous un test. Je ne supposais pas que cela prouverait leur justesse.

**38.** Avec toute la suite, JaCoCo mesure **306/306 instructions** et **28/28 branches**, soit 100 % dans les deux cas. Trois branches manquaient d'abord : le premier mot d'un fichier n'était pas un projet, et une partie de `Gav` valait `null` dans son constructeur. J'ai ajouté ces cas. Le rapport est généré dans `build/reports/jacoco/test/html/index.html`.

**39.** Mesures avec `-PtestLevel=...` (chaque commande régénère le rapport) :

| Niveau seul | Instructions | Branches |
|---|---:|---:|
| Unitaire | 306/306 (100 %) | 28/28 (100 %) |
| Intégration | 267/306 (87,3 %) | 18/28 (64,3 %) |
| Validation | 271/306 (88,6 %) | 18/28 (64,3 %) |

La couverture des tests unitaires est la plus forte ici parce qu'ils visent les cas d'erreur et les branches internes. La validation couvre le chemin métier complet, mais peu de variantes.

**40.** Non. La couverture des instructions correspond au critère « toutes les instructions ». Celle des branches correspond au critère « toutes les issues des décisions ». J'ai essayé un défaut concret : dans `Gav.parse`, transformer le groupe en minuscules avant de créer `Gav`. Les tests existants passaient encore avec **307/307 instructions et 28/28 branches**, alors que `Org.Acme` devenait à tort `org.acme`. Ce défaut résiste donc même à 100 % de couverture des branches, car aucune branche supplémentaire n'est en jeu. J'ai remis le code normal et ajouté ensuite un test qui vérifie la conservation de la casse.

## Partie 7 : bilan

**41.**

| Niveau | Objet du test | Doublures | Exemple de défaut détecté | Localisation |
|---|---|---|---|---|
| Unitaire | Une classe, par exemple le résolveur | Registre Mockito | Boucle sur un cycle | Précise : résolveur |
| Intégration | Interfaces entre classes | Selon l'étape | Mauvaise transmission des dépendances | Quelques classes |
| Validation | Scénario complet depuis un fichier | Aucune | Mauvais résultat final | À rechercher dans le système |

**42.** Le TDD oblige à formuler un exemple vérifiable avant de coder. La triangulation m'a empêché de garder un `Gav.parse` codé pour le premier exemple. Il a aussi aidé pour le résolveur, surtout avec les cycles. En revanche, écrire un test avant chaque petite délégation du lecteur ajoute du temps pour un gain limité. Le risque principal reste de choisir de mauvais exemples : une suite verte et très couverte peut encore oublier un comportement, comme la conservation de la casse.
