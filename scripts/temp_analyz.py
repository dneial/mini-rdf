import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt

# Charger le CSV dans un DataFrame
csv_file_path = "output/analyse/analyse.csv"  # Remplacez par le chemin réel de votre fichier CSV
df = pd.read_csv(csv_file_path)

# Afficher les informations de base sur le DataFrame
print(df.info())

# Analyse 1 : Patrons de requête qui correspondent le mieux au benchmark
# Tri par temps de réponse moyen croissant et par nombre de requêtes sans résultats décroissant
df_sorted = df.sort_values(by=['tmps reponse moyen pattern', 'nb requetes sans resultats'], ascending=[True, False])

# Sélection des meilleurs patrons de requête (vous pouvez ajuster la valeur de 'n' selon vos besoins)
n_top_patterns = 5
top_patterns = df_sorted.head(n_top_patterns)

# Afficher les meilleurs patrons de requête
print("\nMeilleurs patrons de requête pour le benchmark :")
print(top_patterns[['template', 'tmps reponse moyen pattern', 'nb requetes sans resultats']])

# Analyse 2 : Lien entre le nombre de doublons et le temps de réponse des requêtes
plt.figure(figsize=(12, 8))
df['template'] = df['template'].apply(lambda r : r.replace("_10000.queryset", ""))
sns.scatterplot(data=df, x='tmps reponse moyen pattern', y='nb requetes sans resultats', hue='template', palette='tab10', s=100, legend='full')

plt.title('Lien entre le nombre de requetes sans resultats et le temps de réponse')
plt.xlabel('Temps de réponse moyen pattern')
plt.ylabel('nb requetes sans resultats')
plt.show()
