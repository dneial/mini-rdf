import os
import subprocess

memories = [2, 4]
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
        "-m",
        str(memory),
    ]
    subprocess.run(command)
    return

def run():
    for ram in memories:
        for data in dataset:
            print(f"\nexecution sur RDFEngine avec {ram}G de Heap Space et {data} de données\n")
            execute_java_program(f"./data/data/{data}", ram)

# def run(ram, data):
#     print(f"\nexecution sur RDFEngine avec {ram}G de Heap Space et {data} de données\n")
#     execute_java_program(f"./data/data/{data}", ram)

if __name__ == "__main__":
    print("Lancement du benchmark\n")

    # print("veuillez indiquer la quantité de ram à allouer\n")
    # ram = input()
    #
    # print("veuillez indiquer le fichier de données à utiliser (1 ou 2)\n")
    # d = input("1 : 500K.nt\n2 : 2M.nt\n")
    # try :
    #     d = int(d)
    # except ValueError:
    #     print("veuillez entrer un nombre valide")
    #     exit()
    # if d < 0:
    #     print("veuillez entrer un nombre valide")
    #     exit()
    #
    # data = dataset[d-1]
    #
    # run(ram, data)

    run()


