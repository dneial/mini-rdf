import pandas as pd
import matplotlib.pyplot as plt

# Charger le fichier CSV dans un DataFrame
df = pd.read_csv('output/time_monitoring.csv')
#
# # Grouper les données par fichier et calculer la moyenne des temps d'évaluation du workload
# moyennes_par_fichier = df.groupby('Nom du dossier des requêtes')['Temps total d\'évaluation du workload (ms)'].mean()
#
# # Afficher les résultats
# print(moyennes_par_fichier)


# Créer deux sous-ensembles de données pour les deux catégories (distinct et duplicate)
distinct_queries = df[df['Nom du dossier des requêtes'] == './distinctQueries.queryset']
duplicate_queries = df[df['Nom du dossier des requêtes'] == './duplicateQueries.queryset']
# Créer un diagramme à moustache pour les deux catégories sur le même graphe
plt.figure(figsize=(10, 6))

# Positions des boîtes sur l'axe y
positions = [1, 2]

# Diagramme à moustache pour distinctQueries
plt.boxplot(distinct_queries['Temps total d\'évaluation du workload (ms)'], positions=[positions[0]], vert=False, widths=0.6, patch_artist=True, boxprops=dict(facecolor='lightblue'))
plt.title("Comparaison des temps d'évaluation des workloads")
plt.xlabel('Temps d\'évaluation du workload (ms)')

# Diagramme à moustache pour duplicateQueries
plt.boxplot(duplicate_queries['Temps total d\'évaluation du workload (ms)'], positions=[positions[1]], vert=False, widths=0.6, patch_artist=True, boxprops=dict(facecolor='lightgreen'))
plt.yticks(positions, ['requêtes distinctes', 'requête dupliquée'])

# Afficher le graphe
plt.tight_layout()
plt.show()

