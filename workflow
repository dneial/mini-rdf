    Etapes initiales :

1 Compiler WatDiv
    - make clean
    - make

2 Générer les données (1 = 15 MB de données, 10 = 150 MB de données, 100 = 1.5 GB de données)
    - bin/Release/watdiv -d model/wsdbm-data-model.txt 1
    - resultat dans /watdiv-mini-projet/testsuite/dataset/

3 Générer les requetes (nombre à changer dans le script, default = 100)
    - ./regenerate_queryset.sh
    - resultat dans /watdiv-mini-projet/testsuite/queries/

    Tester les requetes et les données sur notre modèle :


elements du csv :

Nom du template de requete,
Nom du fichier de requêtes,
Nom du fichier de données,
Nombre de requêtes,
Nombre de données,
Nombre de requetes sans resultats,


pour chqaue fichier de requete dans dossier (A),
    pour chaque fichier de données dans dossier (B),
        lancer le programme : java -jar RDFEngine.jar -q (A) -d (B) -c (renvoie le nombre de requetes sans resultats)
        insérer le résultat du programme dans le csv