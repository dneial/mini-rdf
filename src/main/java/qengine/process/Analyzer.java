package qengine.process;

import qengine.program.RDFEngine;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Analyzer {
    public static void main(String[] args) throws Exception {
        Path dataPath = Paths.get("/data/data/2M.nt");
        Path templatePath = Paths.get("/data/queries/10000");

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

        //load data
        RDFEngine rdfEngine = new RDFEngine(dataPath.toString(), null);
        rdfEngine.load();

        for (int i = 0; i < 10; i++) {
            Files.walk(templatePath).forEach(path -> {
                if (Files.isRegularFile(path)) {

                    //load les requetes
                    try {
                        rdfEngine.loadQueries(path.toString());
                        tmpslecture.put(path.getFileName().toString(),new ArrayList<>());
                        tmpsreponse.put(path.getFileName().toString(),new ArrayList<>());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    //execute les requetes
                    rdfEngine.run();
                    //recupere les infos
                    tmpslecture.get(path.getFileName().toString()).add(Logger.instance.getReadQueriesTime());
                    tmpsreponse.get(path.getFileName().toString()).add(Logger.instance.getWorkloadTime());

                    //metre les infos une seule fois
                    if (infos.get(path.getFileName().toString()) == null)
                        infos.put(path.getFileName().toString(), new Integer[]{
                                Logger.instance.numQueries,
                                rdfEngine.countDuplicateQueries(),
                                Logger.instance.numQueries - rdfEngine.countDuplicateQueries(),
                                rdfEngine.countEmptyQueries()});
                }
            });
        }


    }
}
