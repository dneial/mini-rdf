import os
import csv
import subprocess
import matplotlib.pyplot as plt
import pandas as pd

csv_headers = [
    "Nom du template de requete",
    "Nom du fichier de requetes",
    "Nom du fichier de données",
    "Nombre de requêtes",
    "Nombre de données",
    "Nombre de requetes sans resultats",
]


# Fonction pour exécuter la commande Java et obtenir le nombre de requêtes sans résultats
def execute_java_program(query_file, data_file):
    command = ["java", "-jar", "RDFEngine.jar", "-q", query_file, "-d", data_file, "-c"]
    result = subprocess.run(command, capture_output=True, text=True)
    return result.stdout.strip()


def run():
    cur_dir = os.getcwd()

    # Dossier contenant les fichiers de requêtes
    query_folder = os.path.abspath(os.path.join(cur_dir, "data/queries/100/"))

    # Dossier contenant les fichiers de données
    data_folder = os.path.abspath(os.path.join(cur_dir, "data/data/"))

    # Chemin du fichier CSV de sortie
    csv_file_path = os.path.abspath(os.path.join(cur_dir, "resultats100.csv"))

    # Entêtes du fichier CSV

    # Ouvrir le fichier CSV en mode écriture
    with open(csv_file_path, mode="w", newline="") as csv_file:
        # Créer un objet writer pour écrire dans le fichier CSV
        csv_writer = csv.writer(csv_file)

        # Écrire les entêtes
        csv_writer.writerow(csv_headers)

        # Parcourir tous les fichiers de requêtes dans le dossier (A)
        for query_file in os.listdir(query_folder):
            query_file_path = os.path.join(query_folder, query_file)

            # Ignorer les sous-dossiers
            if os.path.isfile(query_file_path):
                # Pour chaque fichier de données dans le dossier (B)
                for data_file in os.listdir(data_folder):
                    data_file_path = os.path.join(data_folder, data_file)

                    # Ignorer les sous-dossiers
                    if os.path.isfile(data_file_path):
                        # Exécuter le programme Java et obtenir le nombre de requêtes sans résultats
                        num_queries_without_results = execute_java_program(
                            query_file_path, data_file_path
                        )

                        # Écrire la ligne dans le fichier CSV
                        csv_writer.writerow(
                            [
                                query_file,
                                query_file,
                                data_file,
                                "N/A",
                                "N/A",
                                num_queries_without_results,
                            ]
                        )

        print(f"Le fichier CSV a été créé avec succès : {csv_file_path}")


def plot():
    cur_dir = os.getcwd()

    # Chemin du fichier CSV de sortie
    csv_file_path_100 = os.path.abspath(os.path.join(cur_dir, "resultats100.csv"))

    df_100 = pd.read_csv(csv_file_path_100)

    # x = nom du fichier de requêtes
    # y = nombre de requêtes sans résultats
    #
    df_100["Nombre de requetes sans resultats"] = df_100[
        "Nombre de requetes sans resultats"
    ].astype("int")

    df_100["Nom du fichier de requetes"] = df_100[
        "Nom du fichier de requetes"
    ].str.replace("_100.queryset", "")

    # # Chemin du fichier CSV de sortie
    #  csv_file_path_1000 = os.path.abspath(os.path.join(cur_dir, "resultats1000.csv"))

    #  df_1000 = pd.read_csv(csv_file_path_1000)

    #  # x = nom du fichier de requêtes
    #  # y = nombre de requêtes sans résultats
    #  #
    #  df_1000["Nombre de requetes sans resultats"] = df_1000[
    #      "Nombre de requetes sans resultats"
    #  ].astype("int")

    #  df_1000["Nom du fichier de requetes"] = df_1000[
    #      "Nom du fichier de requetes"
    #  ].str.replace("_1000.queryset", "")

    #  # Chemin du fichier CSV de sortie
    #  csv_file_path_10000 = os.path.abspath(os.path.join(cur_dir, "resultats10000.csv"))

    #  df_10000 = pd.read_csv(csv_file_path_10000)

    #  # x = nom du fichier de requêtes
    #  # y = nombre de requêtes sans résultats
    #  #
    #  df_10000["Nombre de requetes sans resultats"] = df_10000[
    #      "Nombre de requetes sans resultats"
    #  ].astype("int")

    #  df_10000["Nom du fichier de requetes"] = df_10000[
    #      "Nom du fichier de requetes"
    #  ].str.replace("_10000.queryset", "")
    plt.figure(figsize=(10, 10))
    plt.subplot(3, 1, 1)
    plt.bar(
        df_100["Nom du fichier de requetes"],
        df_100["Nombre de requetes sans resultats"],
    )
    plt.title("Nombre de requêtes sans résultats pour 100 requêtes")
    plt.xticks(rotation=35, ha="right")

    plt.subplot(3, 1, 2)

    plt.subplots_adjust(hspace=0.5)
    plt.show()


if __name__ == "__main__":
    plot()
