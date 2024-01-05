import os
import pandas as pd

#recupérer les entêtes : une par ligne dans defs/CSV_HEADER
header = []
with open("defs/CSV_HEADER", "r") as f:
    for line in f:
        header.append(line.strip())

moteur_H         = header[0]
dataFile_H       = header[1]
queryFile_H      = header[2]
rdfTripl_H       = header[3]
queryNb_H        = header[4]
dataReadTime_H   = header[5]
queryReadTime_H  = header[6]
dicoBuildTime_H  = header[7]
indexNb_H        = header[8]
indexBuildTime_H = header[9]
workloadTime_H   = header[10]
totalTime_H      = header[11]


#recupérer les données
data = pd.read_csv("output/bench/time_monitoring.csv", sep=",", header=None, names=header).head(5)

print(data)
