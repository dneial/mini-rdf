package qengine.process;

import qengine.parser.QueryParser;
import qengine.program.RDFEngine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static qengine.process.Logger.readHeader;

public class Analyzer {
    public Path dataPath = Paths.get("data/data/2M.nt");
    public Path templatePath = Paths.get("data/queries/10000");
    public Path outputPath = Paths.get("output/analyse/analyse.csv");

    public void analyseTemp() throws Exception {

        //prend tous les fichiers de requetes dans un dossier
        //les execute sur le moteur
        //cree un CSV avec les infos suivantes :
        //  - le temps de lecture moyen des requetes
        //  - le temps de réponse moyen de chaque pattern
        //  - le nombre de requetes dans le fichier
        //  - le nombre de doublons dans le fichier
        //  - le nombre d' uniques dans le fichier
        //  - le nombre de requetes sans resultats

        Map<String, List<Long>> tmpslecture = new HashMap<>();
        Map<String, List<Long>> tmpsreponse = new HashMap<>();
        Map<String, Integer[]> infos = new HashMap<>();
        AtomicInteger nbreq = new AtomicInteger();
        AtomicInteger nbdouble = new AtomicInteger();
        AtomicInteger nbunique = new AtomicInteger();
        AtomicInteger nbreqsansresult = new AtomicInteger();

        //load data
        RDFEngine rdfEngine = new RDFEngine(dataPath.toString(), "");
        rdfEngine.load();

        for (int i = 0; i < 10; i++) {
            System.out.println("iteration " + i);
            Files.walk(templatePath).forEach(path -> {
                if (Files.isRegularFile(path)) {
                    System.out.println(path);
                    String tempFile = path.getFileName().toString().replaceAll("_1000.queryset", "");

                    //load les requetes
                    try {
                        rdfEngine.loadQueries(path.toString());
                        tmpslecture.put(tempFile, new ArrayList<>());
                        tmpsreponse.put(tempFile, new ArrayList<>());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    //execute les requetes
                    rdfEngine.run();
                    //recupere les infos
                    tmpslecture.get(tempFile).add(Logger.instance.queriesReadTime);
                    tmpsreponse.get(tempFile).add(Logger.instance.workloadEvalTime);

                    //metre les infos une seule fois
                    if (infos.get(path.getFileName().toString()) == null) {

                        nbreq.set(Logger.instance.numQueries);
                        nbdouble.set(rdfEngine.countDuplicateQueries());
                        nbunique.set(nbreq.get() - nbdouble.get());
                        nbreqsansresult.set(rdfEngine.countEmptyQueries());

                        infos.put(path.getFileName().toString(), new Integer[]{
                                nbreq.get(),
                                nbdouble.get(),
                                nbunique.get(),
                                nbreqsansresult.get()});
                    }
                }
            });
        }

        //ecrire les infos dans un CSV
        StringBuilder sb = new StringBuilder();
        List<String> headers = readHeader(2);
        sb.append(String.format("%s,%s,%s,%s,%s,%s,%s\n",
                headers.get(0),
                headers.get(1),
                headers.get(2),
                headers.get(3),
                headers.get(4),
                headers.get(5),
                headers.get(6)));

        for (String key : infos.keySet()) {
            sb.append(String.format("%s,%d,%d,%d,%d,%d,%d\n",
                    key,
                    tmpslecture.get(key).stream().mapToLong(Long::longValue).sum() / tmpslecture.get(key).size(),
                    tmpsreponse.get(key).stream().mapToLong(Long::longValue).sum() / tmpsreponse.get(key).size(),
                    infos.get(key)[0],
                    infos.get(key)[1],
                    infos.get(key)[2],
                    infos.get(key)[3]));
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output/analyse/analyse.csv", false))) {
            // Écrire la ligne de données CSV
            writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createbench() throws Exception {

        //on initialise une liste de requetes
        //pour chaque requète
        //  si elle n'est pas dans la liste et qu'elle a un résultat
        //      on l'ajoute à la liste
        //fin pour
        //on écrit la liste dans un fichier

//        QueryParser qp = new QueryParser(null, "bench.queryset");
//        qp.parseQueries();
//        qp.writeDistinctBench();

        RDFEngine rdfEngine = new RDFEngine(dataPath.toString(), "distinctQueries.queryset");
        rdfEngine.load();


        //garder uniquement les requêtes qui ont des résultats
        rdfEngine.removeEmptyQueries();

    }

    public static void main(String[] args) throws Exception {

        Analyzer analyzer = new Analyzer();
//        analyzer.analyseTemp();
        analyzer.createbench();



    }
}
