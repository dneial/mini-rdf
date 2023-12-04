#RDF Engine

Ce programme Java constitue un moteur de requêtes SPARQL dédié à l'évaluation de requêtes en étoile sur des triplets RDF. Il a été développé dans le cadre de l'UE HAI914I : Gestion des données au-delà de SQL (NoSQL). Le code source est disponible sur GitHub.

##Utilisation
Pour exécuter le programme, vous pouvez utiliser la classe Main avec les options de ligne de commande suivantes :

    -queries (obligatoire) : Chemin vers le fichier contenant les requêtes SPARQL en étoile.
    -data (obligatoire) : Chemin vers le fichier contenant les données RDF.
    -Jena : Active la vérification des résultats avec Apache Jena.
    -warm : Utilise un échantillon de requêtes pour chauffer le système. Spécifiez un pourcentage entre 0 et 100.
    -shuffle : Considère une permutation aléatoire des requêtes.
    -output : Chemin vers le fichier de sortie pour afficher différentes mesures associées au fonctionnement de l'outil.
    -export_results : Enregistre les résultats des requêtes dans un fichier CSV.

Exemple d'utilisation :

```bash

java -jar RDFEngine.jar -queries chemin/vers/requetes.sparql -data chemin/vers/donnees.rdf -Jena -output chemin/vers/sortie -export_results chemin/vers/resultats.csv
```
##Fonctionnalités

    -Chargement efficace des données RDF et des requêtes.
    -Évaluation des requêtes en étoile avec gestion intelligente des options.
    -Validation des résultats avec Apache Jena.
    -Simulation d'un environnement d'exécution "chaud" avec l'option -warm.
    -Permutation aléatoire des requêtes avec l'option -shuffle.
    -Mesures détaillées affichées dans la console et enregistrées dans un fichier CSV avec l'option -output.
    -Export des résultats des requêtes dans un fichier CSV avec l'option -export_results.

###Auteurs

Ce projet a été réalisé par Daniel Azevedo Gomes et Sébastien Prud’homme Gateau dans le cadre de l'UE HAI914I.
