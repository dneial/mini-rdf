import os
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np


#recupérer les entêtes : une par ligne dans defs/CSV_HEADER
header = []
with open("defs/CSV_HEADER", "r") as f:
    for line in f:
        header.append(line.strip())

moteur_H         = header[0]
memoire_H        = header[1]
dataFile_H       = header[2]
queryFile_H      = header[3]
rdfTripl_H       = header[4]
queryNb_H        = header[5]
dataReadTime_H   = header[6]
queryReadTime_H  = header[7]
dicoBuildTime_H  = header[8]
indexNb_H        = header[9]
indexBuildTime_H = header[10]
workloadTime_H   = header[11]
totalTime_H      = header[12]


#recupérer les données
data = pd.read_csv("output/bench/time_monitoring.csv", sep=",", header=None, names=header).head(5)


# Définir les scénarios
scenarios = [
    {"donnees": 500000, "memoire": 2},
    {"donnees": 500000, "memoire": 4},
    {"donnees": 500000, "memoire": 8},
    {"donnees": 2000000, "memoire": 2},
    {"donnees": 2000000, "memoire": 4},
    {"donnees": 2000000, "memoire": 8},
]

# Créer un diagramme avec trois sous-graphes
fig, axs = plt.subplots(3, 1, figsize=(10, 18))

# Titre du diagramme
fig.suptitle("Comparaison Jena et Moteur-rdf", fontsize=16)

categories = [dataReadTime_H, workloadTime_H, totalTime_H]

for i, category in enumerate(categories):
    # Barres pour Jena
    jena_values = data[data["moteur"] == "Jena"][category]
    axs[i].bar(np.arange(len(jena_values)) - 0.2, jena_values, width=0.4, label="Jena")

    # Barres pour Moteur-rdf
    moteur_rdf_values = data[data["moteur"] == "Moteur-rdf"][category]
    axs[i].bar(np.arange(len(moteur_rdf_values)) + 0.2, moteur_rdf_values, width=0.4, label="Moteur-rdf")

    # Réglages des axes
    axs[i].set_title(f"{category}")
    axs[i].set_xlabel("Moteur")
    axs[i].set_ylabel("Temps (ms)")
    axs[i].set_xticks(np.arange(len(data["moteur"])))
    axs[i].set_xticklabels(data["moteur"])
    axs[i].legend()

# Ajustements de la mise en page
plt.tight_layout(rect=[0, 0, 1, 0.96])
plt.show()

