import os
import subprocess


### éléments à varier lors des expériences :
###     moteur (Jena, RDFEngine)
###     mémoire allouée (heap space : -Xmx(n)g avec n = 2, 4, 8)
###     data set (500k.nt ou 2M.nt)

### total : 12 expériences possibles

<<<<<<< HEAD
memories = [1, 2, 4, 8]
dataset = ["500K.nt", "2M.nt"]
querypath = "./data/queries/benchmark.queryset"
=======
memories = [2, 4, 8]
dataset = ['500K.nt', '2M.nt']
querypath = './data/queries/benchmark.queryset'
>>>>>>> origin/master


# Fonction pour exécuter la commande Java et obtenir le nombre de requêtes sans résultats
def execute_java_program(data_file, memory):
<<<<<<< HEAD
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
=======
    command = ["/home/e20170009949/.jdks/openjdk-21.0.1/bin/java", f"-Xmx{memory}g", "-jar", "RDFEngine.jar", "-q", querypath, "-d", data_file, "-o", "output/bench/"]
>>>>>>> origin/master
    subprocess.run(command)
    return

def execute_jena_program(data_file, memory):
    command = ["/home/e20170009949/.jdks/openjdk-21.0.1/bin/java", f"-Xmx{memory}g", "-jar", "RDFEngine.jar", "-q", querypath, "-d", data_file, "-jc" , "output/bench/"]
    subprocess.run(command)
    return

def run():
    for m in memories:
        for d in dataset:
#             print(f"\nexecution sur RDFEngine avec {m}g et {d}\n")
#             execute_java_program(f"./data/data/{d}", m)

            print(f"\nexecution sur RDFEngine avec {m}G de Heap Space et {d}\n")
            execute_jena_program(f"./data/data/{d}", m)


if __name__ == "__main__":
    run()
