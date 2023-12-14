package qengine.program;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;
import org.eclipse.rdf4j.query.algebra.StatementPattern;
import qengine.parser.DataParser;
import qengine.parser.QueryParser;
import qengine.process.SearchEngine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class RDFEngine {
    DataParser dataParser;
    QueryParser queryParser;
    SearchEngine searchEngine;

    public RDFEngine(String dataPath, String queriesPath) {
        this.dataParser = new DataParser(null, dataPath);
        this.queryParser = new QueryParser(null, queriesPath);
        this.searchEngine = new SearchEngine();
    }

    public void load() throws Exception {
        this.searchEngine.initData(this.dataParser);
        this.queryParser.parseQueries();
    }

    public void runJenaValidation() {
        // Activer la vérification avec Jena
        // Utiliser Jena comme un oracle

        // Créer un modèle vide
        Model model = ModelFactory.createDefaultModel();

        // Charger le fichier RDF dans le modèle
        FileManager.get().readModel(model, dataParser.getDataFile());
        int cpt = 0;
        int score = 0;
        // pour chaque requête du fichier de requêtes
        for (String query : queryParser.getStrQueries()) {

            // Créer une requête Jena
            Query jenaQuery = QueryFactory.create(query);

            // Exécuter la requête sur le modèle
            try (QueryExecution qexec = QueryExecutionFactory.create(jenaQuery, model)) {
                ResultSet jenaResults = qexec.execSelect();
                score += compareResults(
                        jenaResults,
                        searchEngine.query(queryParser.getQueries().get(cpt++)),
                        cpt
                );

            } catch (Exception e) {
                // Gérer l'exception ici
                e.printStackTrace();
            }
        }

        System.out.println("Score : " + score + "/" + queryParser.getStrQueries().size());
    }


    private static int compareResults(ResultSet jenaResults, List<String> results2, int i) {

//        System.out.println("Comparaison des résultats query ("+i+")\n");

        ArrayList<String> results1 = new ArrayList<>();
        while (jenaResults.hasNext()) {
            QuerySolution soln = jenaResults.nextSolution();
            results1.add(soln.get("v0").toString());
        }

        if (results1.size() != results2.size()) {
            return 0;
        }
        else {
            //intersection entre les deux listes
            results1.retainAll(results2);

            if (results1.size() != results2.size()) {
                return 0;
            }
        }
        return 1;
    }

    public void warmup(int percentage){

        List<List<StatementPattern>> queries = queryParser.getQueries();
        // Calculer le nombre de requêtes à inclure dans l'échantillon
        int numQueries = (percentage * queries.size()) / 100 + 1;

        // System.out.println("Nombre de requêtes : " + numQueries + "/" + queries.size());

        // Créer un échantillon aléatoire de requêtes
        List<List<StatementPattern>> warmupQueries = new ArrayList<>();
        List<String> warmupStrQueries = new ArrayList<>();
        for (int i = 0; i < numQueries; i++) {
            int randomIndex = (int) (Math.random() * queries.size());
            warmupQueries.add(queries.get(randomIndex));
            warmupStrQueries.add(queryParser.getStrQueries().get(randomIndex));
        }

        Map<List<StatementPattern>, List<String>> results;

        // Utiliser l'échantillon pour chauffer le système
        // Exécuter les requêtes sur votre moteur RDF
        results = searchEngine.queryAll(warmupQueries);

        // Continuer avec le reste du traitement en utilisant l'échantillon warmupQueries

    }

    public void shuffle() {
        queryParser.shuffle();
    }

    public void run() {
        searchEngine.queryAll(queryParser.getQueries());
        // searchEngine.displayResults();
    }

    public int runCount() {
        searchEngine.queryAll(queryParser.getQueries());
        return searchEngine.emptyQueries;
    }


    public void dumpResults(String directoryPath) throws IOException {

        //check if directory passed in filename exists
        // timestamp
        String timestamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());


        String FILENAME = "results" + timestamp + ".csv";
        String outputPath = directoryPath + "/" + FILENAME;
        //check if an output file exists in directory
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writer.write("Query ; Results\n");
            for (Map.Entry<List<StatementPattern>, List<String>> entry : searchEngine.getResults().entrySet()) {
                StringBuilder prettyQuery = new StringBuilder("SELECT ?v0 WHERE { ");
                for (StatementPattern statementPattern : entry.getKey()) {
                    prettyQuery
                            .append(statementPattern.getPredicateVar().getValue())
                            .append(" ")
                            .append(statementPattern.getObjectVar().getValue())
                            .append(" .");
                }
                prettyQuery.append(" }");
                writer.write(String.format("%s ; %s\n",
                        prettyQuery, searchEngine.getResults().get(entry.getKey())));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printQueryInfo() {
        System.out.println("Nombre de requêtes : " + queryParser.getQueries().size());
        //nombre de requetes doubles
        System.out.println("Nombre de requêtes dupliquées : " + queryParser.getNumberOfDuplicateQueries());
    }

}
