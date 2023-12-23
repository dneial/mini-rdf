import pandas as pd


def run():
    df = pd.read_csv("output/bench/time_monitoring.csv")
    df_jena = df[df["moteur"] == "jena"]
    df_rdfengine = df[df["moteur"] == "qengine"]
    jena_times = df_jena["tps total workload (ms)"]
    rdfengine_times = df_rdfengine["tps total workload (ms)"]


if __name__ == "__main__":
    run()
