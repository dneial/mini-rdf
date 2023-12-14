package qengine.process;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    public static Logger instance = new Logger();
    public boolean active;
    public String dataPath;
    public String queriesPath;
    public int dataTriplets;
    public int numQueries;
    public long dataReadTime;
    public long queriesReadTime;
    public long dictCreationTime;
    public int numIndexes = 6;
    public long indexCreationTime;
    public long workloadEvalTime;
    public long startTime;
    public long totalTime;
    public String outputPath;
    private long dictLapTime = 0;
    private long hexaLapTime = 0;

    public Logger() {
        this.active = false;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public void setQueriesPath(String queriesPath) {
        this.queriesPath = queriesPath;
    }

    public void setNumQueries(int numQueries) {
        this.numQueries = numQueries;
    }

    public void startTime() {
        this.startTime = System.currentTimeMillis();
    }

    public void setOutputPath(String directoryPath) {

        String FILENAME = "time_monitoring.csv";
        this.outputPath = directoryPath + "/" + FILENAME;
        //check if an output file exists in directory
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(outputPath);

        if (!file.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
                // Écrire l'en-tête CSV
                writer.write("Nom du fichier de données,Nom du dossier des requêtes,Nombre de triplets RDF," +
                        "Nombre de requêtes,Temps de lecture des données (ms),Temps de lecture des requêtes (ms)," +
                        "Temps création dico (ms),Nombre d'index,Temps de création des index (ms)," +
                        "Temps total d'évaluation du workload (ms),Temps total (du début à la fin du programme) (ms)\n");

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void log(String dataPath, String queriesPath, int dataTriplets, int numQueries,
                    long dataReadTime, long queriesReadTime, long dictCreationTime, int numIndexes,
                    long indexCreationTime, long workloadEvalTime, long totalTime, String outputPath) {
        if (active) {
            exportToCSV(dataPath, queriesPath, dataTriplets, numQueries, dataReadTime, queriesReadTime,
                    dictCreationTime, numIndexes, indexCreationTime, workloadEvalTime, totalTime, outputPath);
        }
    }

    private static void exportToCSV(String dataPath, String queriesPath, int dataTriplets, int numQueries,
                                    long dataReadTime, long queriesReadTime, long dictCreationTime, int numIndexes,
                                    long indexCreationTime, long workloadEvalTime, long totalTime, String outputPath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath, true))) {
            // Écrire la ligne de données CSV
            writer.write(String.format("%s,%s,%d,%d,%d,%d,%d,%d,%d,%d,%d\n",
                    dataPath, queriesPath, dataTriplets, numQueries, dataReadTime, queriesReadTime,
                    dictCreationTime, numIndexes, indexCreationTime, workloadEvalTime, totalTime));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startReadDataTime() {
        this.dataReadTime = System.currentTimeMillis();
    }

    public void stopReadDataTime() {
        this.dataReadTime = System.currentTimeMillis() - this.dataReadTime;
    }

    public void dicoCreateLapStart() {
        this.dictLapTime = System.currentTimeMillis();
    }

    public void dicoCreateLapEnd() {
        this.dictLapTime = System.currentTimeMillis() - this.dictLapTime;
        this.dictCreationTime += this.dictLapTime;
    }

    public void hexaCreateLapStart() {
        this.hexaLapTime = System.currentTimeMillis();
    }

    public void hexaCreateLapEnd() {
        this.hexaLapTime = System.currentTimeMillis() - this.hexaLapTime;
        this.indexCreationTime += this.hexaLapTime;
    }

    public void dump() {
        //write to file
        log(dataPath, queriesPath, dataTriplets, numQueries, dataReadTime, queriesReadTime, dictCreationTime,
                numIndexes, indexCreationTime, workloadEvalTime, totalTime, outputPath);

        //dump to console
        System.out.println("Nom du fichier de données: " + dataPath);
        System.out.println("Nom du dossier des requêtes: " + queriesPath);
        System.out.println("Nombre de triplets RDF: " + dataTriplets);
        System.out.println("Nombre de requêtes: " + numQueries);
        System.out.println("Temps de lecture des données (ms): " + dataReadTime);
        System.out.println("Temps de lecture des requêtes (ms): " + queriesReadTime);
        System.out.println("Temps création dico (ms): " + dictCreationTime);
        System.out.println("Nombre d'index: " + numIndexes);
        System.out.println("Temps de création des index (ms): " + indexCreationTime);
        System.out.println("Temps total d'évaluation du workload (ms): " + workloadEvalTime);
    }

    public void countRdfTriple() {
        this.dataTriplets++;
    }

    public void startReadQueriesTime() {
        this.queriesReadTime = System.currentTimeMillis();
    }
    public void stopReadQueriesTime() {
        this.queriesReadTime = System.currentTimeMillis() - this.queriesReadTime;
    }

    public void startWorkloadEvalTime() {
        this.workloadEvalTime = System.currentTimeMillis();
    }

    public void stopWorkloadEvalTime() {
        this.workloadEvalTime = System.currentTimeMillis() - this.workloadEvalTime;
    }

    public void stopTotalTime() {
        this.totalTime = System.currentTimeMillis() - this.startTime;
    }
}
