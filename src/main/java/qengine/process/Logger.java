package qengine.process;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Logger {

    //Singleton
    public static Logger instance = new Logger();

    //if Logger is in use
    public boolean active;

    //header for the csv
    public List<String> HEADER;

    //data to log
    public String moteur;
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
    public long totalTime;

    //computing attributes
    public long startTime;
    public String outputPath;
    private long dictLapTime = 0;
    private long hexaLapTime = 0;

    //Setters
    private Logger() {
        this.active = false;
    }

    public void setActive(boolean active) {
        this.active = active;
        this.HEADER = readHeader(1);
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public void setQueriesPath(String queriesPath) {
        this.queriesPath = queriesPath;
    }

    public void setNumTriplets(long size) {
        this.dataTriplets = (int) size;
    }

    public void setNumQueries(int numQueries) {
        this.numQueries = numQueries;
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
                // Écrire l'en-tete CSV
                writer.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                        this.HEADER.get(0), this.HEADER.get(1), this.HEADER.get(2), this.HEADER.get(3),
                        this.HEADER.get(4), this.HEADER.get(5), this.HEADER.get(6), this.HEADER.get(7),
                        this.HEADER.get(8), this.HEADER.get(9), this.HEADER.get(10), this.HEADER.get(11)
                ));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void countRdfTriple() {
        this.dataTriplets++;
    }

    //time methods
    public void startTime() {
        this.startTime = System.currentTimeMillis();
    }
    public void stopTotalTime() {
        this.totalTime = System.currentTimeMillis() - this.startTime;
    }

    public void startReadDataTime() {
        this.dataReadTime = System.currentTimeMillis();
    }
    public void stopReadDataTime() {
        this.dataReadTime = System.currentTimeMillis() - this.dataReadTime;
    }

    public void startReadQueriesTime() {
        this.queriesReadTime = System.currentTimeMillis();
    }
    public void stopReadQueriesTime() {
        this.queriesReadTime = System.currentTimeMillis() - this.queriesReadTime;
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

    public void startWorkloadEvalTime() {
        this.workloadEvalTime = System.currentTimeMillis();
    }
    public void stopWorkloadEvalTime() {
        this.workloadEvalTime = System.currentTimeMillis() - this.workloadEvalTime;
    }


    //reset the logger
    public static void reset() {
        instance.moteur = "";
        instance.dataTriplets = 0;
        instance.numQueries = 0;
        instance.dataReadTime = 0;
        instance.queriesReadTime = 0;
        instance.dictCreationTime = 0;
        instance.numIndexes = 6;
        instance.indexCreationTime = 0;
        instance.workloadEvalTime = 0;
        instance.startTime = 0;
        instance.totalTime = 0;
    }

    //log methods
    public static List<String> readHeader(int logType) {
        String headersPath = "";

        switch (logType)
        {
            case 1:
                headersPath = "defs/CSV_HEADER";
                break;
            case 2:
                headersPath = "defs/ANALYSIS_HEADER";
                break;
            default:
                return null;
        }

        ArrayList<String> headersList = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File(headersPath));
            while (scanner.hasNextLine()) {
                headersList.add(scanner.nextLine());
            }
            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return headersList;
    }

    public void log(String dataPath, String queriesPath, int dataTriplets, int numQueries,
                    long dataReadTime, long queriesReadTime, long dictCreationTime, int numIndexes,
                    long indexCreationTime, long workloadEvalTime, long totalTime, String outputPath) {
        if (active) {
            exportToCSV(dataPath, queriesPath, dataTriplets, numQueries, dataReadTime, queriesReadTime,
                    dictCreationTime, numIndexes, indexCreationTime, workloadEvalTime, totalTime, outputPath);
        }
    }

    public void csvlog(){

        try (FileWriter csvWriter = new FileWriter(new File(Logger.instance.outputPath), true)) {

            // Si le fichier est nouvellement créé, écrire les en-têtes
            if (new File(outputPath).length() == 0) {
                for (String header : HEADER) {
                    csvWriter.append(header).append(",");
                }
                csvWriter.append("\n");
            }

            System.out.println(String.format("%s,%s,%s,%d,%d,%d,%d,%d,%d,%d,%d,%d\n",
                    moteur, dataPath, queriesPath, dataTriplets, numQueries, dataReadTime, queriesReadTime,
                    dictCreationTime, numIndexes, indexCreationTime, workloadEvalTime, totalTime));

            csvWriter.write(String.format("%s,%s,%s,%d,%d,%d,%d,%d,%d,%d,%d,%d\n",
                    moteur, dataPath, queriesPath, dataTriplets, numQueries, dataReadTime, queriesReadTime,
                    dictCreationTime, numIndexes, indexCreationTime, workloadEvalTime, totalTime));

        } catch (IOException e) {
            // Gérer les exceptions liées à l'écriture du fichier
            e.printStackTrace();
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


    public void dump() {
        //write to file
        if (!active) return;

        csvlog();
    }




}
