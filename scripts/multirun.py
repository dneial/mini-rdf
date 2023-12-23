import os
import subprocess


# éléments à varier lors des expériences :
#     moteur (Jena, RDFEngine)
#     mémoire allouée (heap space : -Xmx(n)g avec n = 2, 4, 8)
#     data set (500k.nt ou 2M.nt)

# total : 12 expériences possibles

memories = [2, 4, 8]
dataset = ["500K.nt", "2M.nt"]
querypath = "./data/queries/benchmark.queryset"


# Fonction pour exécuter la commande Java et obtenir le nombre de requêtes sans résultats
def execute_java_program(data_file, memory):
    command = [
        "java",
        f"-Xmx{memory}g",
        "-jar",
        "RDFEngine.jar",
        "-q",
        querypath,
        "-d",
        data_file,
        "-o",
        "output/bench/",
    ]
    subprocess.run(command)
    return


def execute_jena_program(data_file, memory):
    command = [
        "java",
        f"-Xmx{memory}g",
        "-jar",
        "RDFEngine.jar",
        "-q",
        querypath,
        "-d",
        data_file,
        "-jr",
        "output/bench/",
    ]
    subprocess.run(command)
    return


def run():
    for m in memories:
        for d in dataset:
            print(f"\nexecution sur RDFEngine avec {m}g de Heap Space et {d}\n")
            execute_jena_program(f"./data/data/{d}", m)


if __name__ == "__main__":
    run()
