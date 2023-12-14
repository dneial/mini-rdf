import os
import csv
import subprocess
import pandas as pd

# lire les fichiers CSV dans le dossier resultats
# si nbReq - (nbReqSansRes) < 60% alors on garde le fichier
# on concatene les fichiers gardés dans un seul fichier
# => fichier de benchmark

def findfile(query_file):

    # Dossiers à verifier
    queries_folder = os.path.abspath(os.path.join(os.getcwd(), "data/queries/"))

    # Parcourir les dossiers
    for folder in os.listdir(queries_folder):
        folder_path = os.path.join(queries_folder, folder)

        # Ignorer les sous-dossiers
        if os.path.isdir(folder_path):

            # Parcourir les fichiers dans le dossier
            for file in os.listdir(folder_path):
                file_path = os.path.join(folder_path, file)

                # Ignorer les sous-dossiers
                if os.path.isfile(file_path):

                    # Si le nom du fichier est celui recherché, retourner le chemin
                    if file == query_file:
                        return file_path

    # Si le fichier n'est pas trouvé, retourner None
    return None


def run():
    resultats_folder = os.path.abspath(os.path.join(os.getcwd(), "resultats/"))

    bench_file = os.path.abspath(os.path.join(os.getcwd(), "resultats/benchmark.queryset"))

    #parcourir les fichiers dans le dossier resultats
    for resfile in os.listdir(resultats_folder):

        #lire le fichier CSV
        df = pd.read_csv(os.path.join(resultats_folder, resfile))

        #calculer le nombre de requetes sans resultats
        df["Nombre de requetes sans resultats"] = df["Nombre de requetes sans resultats"].astype(int)
        df["Nombre de requetes"] = df["Nombre de requêtes"].astype(int)

        #pour chaque fichier, calculer le pourcentage de requetes sans resultats
        perte = df["Nombre de requetes sans resultats"] / df["Nombre de requetes"]

        #garder les fichiers dont le pourcentage de perte est inferieur à 60%
        dfKeep = df[perte < 0.6]

        #ajouter les fichiers à la liste des fichiers à garder
        filesToKeep = [f for f in dfKeep["Nom du fichier de requetes"].unique()]

    #concatener le contenu des fichiers à garder dans un seul fichier
    with open(bench_file, "a+") as outfile:
        for fname in filesToKeep:
            f = findfile(fname)
            if f is None:
                print(f"Le fichier {fname} n'a pas été trouvé")
                continue

            with open(f, "r") as infile:
                outfile.write(infile.read())
            outfile.write("\n")

if __name__ == "__main__":
    run()
