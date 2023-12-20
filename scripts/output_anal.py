import pandas as pd
import os
import matplotlib.pyplot as plt

def boxplot_distinct_vs_duplicate():
    # Charger le fichier CSV dans un DataFrame
    df = pd.read_csv('output/time_monitoring.csv')
    #
    # # Grouper les donnees par fichier et calculer la moyenne des temps d'evaluation du workload
    # moyennes_par_fichier = df.groupby('Nom du dossier des requetes')['Temps total d\'evaluation du workload (ms)'].mean()
    #
    # # Afficher les resultats
    # print(moyennes_par_fichier)


    # Creer deux sous-ensembles de donnees pour les deux categories (distinct et duplicate)
    distinct_queries = df[df['Nom du dossier des requetes'] == './distinctQueries.queryset']
    duplicate_queries = df[df['Nom du dossier des requetes'] == './duplicateQueries.queryset']
    # Creer un diagramme à moustache pour les deux categories sur le meme graphe
    plt.figure(figsize=(10, 6))

    # Positions des boîtes sur l'axe y
    positions = [1, 2]

    # Diagramme à moustache pour distinctQueries
    plt.boxplot(distinct_queries['Temps total d\'evaluation du workload (ms)'], positions=[positions[0]], vert=False, widths=0.6, patch_artist=True, boxprops=dict(facecolor='lightblue'))
    plt.title("Comparaison des temps d'evaluation des workloads")
    plt.xlabel('Temps d\'evaluation du workload (ms)')

    # Diagramme à moustache pour duplicateQueries
    plt.boxplot(duplicate_queries['Temps total d\'evaluation du workload (ms)'], positions=[positions[1]], vert=False, widths=0.6, patch_artist=True, boxprops=dict(facecolor='lightgreen'))
    plt.yticks(positions, ['requetes distinctes', 'requete dupliquee'])

    # Afficher le graphe
    plt.tight_layout()
    plt.show()


def boxplot_files(files_to_compare):
    ''' Affiche un diagramme à moustache pour chaque fichier dans files_to_compare'''

    # Charger le fichier CSV dans un DataFrame
    df = pd.read_csv('output/template_comparison/time_monitoring.csv')

    # Creer un sous-ensemble de donnees pour les fichiers à comparer
    selected_data = df

    print(selected_data)

    # Creer un diagramme à moustache pour chaque fichier sur le meme graphe
    plt.figure(figsize=(12, 8))

    # Positions des boîtes sur l'axe y
    positions = range(1, len(files_to_compare) + 1)

    # Pour chaque fichier à comparer
    for i, file in enumerate(files_to_compare):
        file_data = selected_data[selected_data['Nom du fichier de requêtes'] == file]
        plt.boxplot(file_data["Temps total d'évaluation du workload (ms)"], positions=[positions[i]],
                    vert=False, widths=0.6, patch_artist=True,
                    boxprops=dict(facecolor=f'lightblue' if i % 2 == 0 else 'lightgreen'))

    plt.title("Comparaison des temps d'évaluation des workloads")
    plt.xlabel('Temps d\'évaluation du workload (ms)')
    plt.yticks(positions, files_to_compare)

    # Afficher le graphe
    plt.tight_layout()
    plt.show()



def histogram_files(files_to_compare):
    '''Affiche un histogramme des moyennes des temps d'évaluation du workload pour chaque fichier'''

    # Charger le fichier CSV dans un DataFrame
    df = pd.read_csv('output/template_comparison/time_monitoring.csv')
    # retirer ".queryset" de chaque nom de fichier de la colone "Nom du fichier de requêtes"
    df["Nom du fichier de requêtes"] = df["Nom du fichier de requêtes"].apply(lambda r: r.replace('_10000.queryset', ''))
    # Creer un sous-ensemble de donnees pour les fichiers à comparer
    selected_data = df

    # Calculer les moyennes par fichier
    mean_times = selected_data.groupby('Nom du fichier de requêtes')["Temps total d'évaluation du workload (ms)"].mean()

    # Creer un histogramme
    plt.figure(figsize=(10, 6))
    plt.bar(mean_times.index, mean_times, color=['lightblue' if i % 2 == 0 else 'lightgreen' for i in range(len(mean_times))])
    plt.title("Histogramme des moyennes des temps d'évaluation des workloads (10000 requêtes/500k données)")
    plt.xlabel('Patrons de requêtes')
    plt.ylabel('Moyenne des temps d\'évaluation du workload (ms)')
    plt.xticks(rotation=45, ha='right')
    plt.tight_layout()
    plt.yscale('log')

    plt.show()


if __name__ == "__main__":
    files_to_compare = os.listdir(('data/queries/10000'))
#     boxplot_files(files_to_compare)
    histogram_files([f.replace('.queryset', '') for f in files_to_compare])

