import os
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np


#recuperer les entêtes : une par ligne dans defs/CSV_HEADER
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


#recuperer les donnees
data = pd.read_csv("output/bench/time_monitoring.csv", sep=",", header=None, names=header)
data['temps de lecture'] = data[dataReadTime_H] + data[queryReadTime_H]

# Creation du groupe "sex", et decompte des individus de chaque sous-grouê (male, female)
g = data.groupby([memoire_H,dataFile_H])
# print(g.groups)

g1 = g.get_group(('2', './data/data/500K.nt'))
g2 = g.get_group(('2', './data/data/2M.nt'))
g3 = g.get_group(('4', './data/data/500K.nt'))
g4 = g.get_group(('4', './data/data/2M.nt'))

# categories
# tmps lecture
# tmps workload
# tmps total


# On extrait les noms des differents hairpattern et des differents sexes
categories = ['temps de lecture des requêtes', 'temps d\'évalutaion du workload', 'temps total']
moteur = data[moteur_H].value_counts().index
pos = np.arange(len(categories))
width = 0.35  # epaisseur de chaque bâton

# données
qenginedata = g4.groupby(moteur_H).get_group('qengine')[[queryReadTime_H,workloadTime_H,totalTime_H]]
jenadata = g4.groupby(moteur_H).get_group('jena')[[queryReadTime_H,workloadTime_H,totalTime_H]]

qenginedata = qenginedata.transpose().squeeze().astype('int32')
jenadata = jenadata.transpose().squeeze().astype('int32')

print(jenadata)
print(qenginedata)

# Creation du diagramme en bâtons (bâtons côte à côte)
plt.bar(pos - width/2, qenginedata, width, color='IndianRed')
plt.bar(pos + width/2, jenadata, width, color='SkyBlue')
plt.xticks(pos, categories)
plt.title('comparaison des temps d\'executions des moteurs pour 2M données et 2G de mémoire',fontsize=15)
plt.legend(moteur,loc=1)
plt.show()
