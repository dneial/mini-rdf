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


pour chaque fichier de requete dans dossier (A),
    pour chaque fichier de données dans dossier (B),
        lancer le programme : java -jar RDFEngine.jar -q (A) -d (B) -c (renvoie le nombre de requetes sans resultats)
        insérer le résultat du programme dans le csv




Log 14/12/2023 18:08:30

scripts :
    tests_données.py :
        [pour chaque fichier de requete dans dossier (A),
            pour chaque fichier de données dans dossier (B),
                lancer le programme : java -jar RDFEngine.jar -q (A) -d (B) -c (renvoie le nombre de requetes sans resultats)
                insérer le résultat du programme dans le csv
        ]

    select_queries.py :
        [filtre les patrons de requete ayant moins de 60% de perte
        (nombre de requetes sans resultats > 60% du nombre de requetes total)
        et les réunit dans un seul fichier de requetes : benchmark.queryset
        ]

    output_anal.py :
        [
            visualise les différences de temps de réponse entre les executions de queryset ayant des doublons ou pas
        ]

Ajouts fonctionnalités:

    option -qi : montre le nombre de doublons dans le fichier de requetes utilisé
    QueryParser.writeDistinctBench : crée 2 fichier de requetes :
        - distinctQueries.queryset : requetes sans doublons
        - duplicateQueries.queryset : requetes avec doublons

    DEDUCTION : LES DOUBLONS CA INFLUE SUR LE TEMPS DE REPONSE ??


    ON VEUT:
    - analyser les temps de réponse des différents patterns de requetes
    plot chaque pattern

    errer




    $wsdwdrwgrbmkj cfbklqhhpibh
