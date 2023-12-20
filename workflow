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

Log 15/12

    il faut à terme
        une option benchmark qui prend en paramètre un fichier de requetes et un fichier de données
        et qui renvoie

        les différentes infos liées à l'execution de notre moteur :
            - temps de lecture du fichier de données
            - temps de lecture du fichier de requetes
            - temps du workload
            - temps total de l'execution

        les temps en lien aux données :
            - temps de création du dictionnaire
            - temps de création de l'index
            - nombre de doublons dans le fichier de requetes
            - nombre de requetes sans resultats



        une option d'analyse qui va nous permettre de créer notre benchmark_requete

         benchmark_requete doit contenir des requetes pertinantes :
         - varier les patrons de requetes
         - peu de requetes sans resultats (ou alors elles doivent faire tourner le moteur longtemps)


        on à donc besoin comme infos sur les patterns :
        - le temps de lecture moyen des requetes
        - le temps de réponse moyen de chaque pattern
        - le nombre de requetes dans le fichier
        - le nombre de doublons dans le fichier
        - le nombre d' uniques dans le fichier
        - le nombre de requetes sans resultats


    Misc TODO :
        - retirer strQueries de QueryParser
            => si option Jena transformer les requetes en string plutot



    on prend : requetes qui ont un résultat


On remarque :
    peu de résultats => temps de réponse très court
        explication :
            - pour les requêtes composées on stoppe execution dès qu'un pattren ne renvoie rien

        utilité :
            pour benchmark on veut vitesse d'execution
            => on veut des requetes qui ont un résultat



        Base de requetes 1 :
            pas de doublons
            pas de requetes sans resultats

        Base de requetes 2 :
            pas de doublons
            peu de requetes sans resultats
            on garde quand même les Q4 pour avoir de la variété