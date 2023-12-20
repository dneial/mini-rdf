import os
import subprocess


### éléments à varier lors des expériences :
###     mémoire allouée (heap space : -Xmx(n)g avec n = 1, 2, 4, 8)
###     OS (Windows, Linux)
###     data set (500k.nt ou 2M.nt)

### total : 16 expériences possibles
## script en réalise 8 (OS à varier manuellement)

memories = [1, 2, 4, 8]
dataset = ['500k.nt', '2M.nt']
querypath = './data/queries/benchmark.queryset'


# Fonction pour exécuter la commande Java et obtenir le nombre de requêtes sans résultats
def execute_java_program(data_file, memory):
    command = ["java", f"-Xmx{memory}g", "-jar", "RDFEngine.jar", "-q", querypath, "-d", data_file, "-o", "output/bench/"]
    subprocess.run(command)
    return


def run():
    for m in memories:
        for d in dataset:
            print(f"\nexecution avec {m}g et {d}\n")
            execute_java_program(f"./data/data/{d}", m)

if __name__ == "__main__":
    run()