package qengine.program;

import org.apache.commons.cli.*;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;
import org.eclipse.rdf4j.query.algebra.StatementPattern;
import qengine.parser.QueryParser;
import qengine.process.SearchEngine;
import qengine.parser.DataParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {

        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        // Définition des options

        //options obligatoires
        Option queryOpt = new Option("queries", true, "Chemin vers le fichier contenant les requêtes");
        queryOpt.setRequired(true);
        options.addOption(queryOpt);

        Option dataOpt = new Option("data", true, "Chemin vers le fichier contenant les données");
        dataOpt.setRequired(true);
        options.addOption(dataOpt);

        //options facultatives
        options.addOption("Jena", false, "Active la vérification avec Jena");
        options.addOption("warm", true, "Utilise un échantillon de requêtes pour chauffer le système");
        options.addOption("shuffle", false, "Considère une permutation aléatoire des requêtes");
        options.addOption("output", true, "Chemin vers le fichier de sortie");

        try {
            CommandLine cmd = parser.parse(options, args);

            String queriesPath = cmd.getOptionValue("queries");
            String dataPath = cmd.getOptionValue("data");
            String outputPath = cmd.getOptionValue("output");


            DataParser dataParser = new DataParser(null, dataPath);
            QueryParser queryParser = new QueryParser(null, queriesPath);

            // Récupérer les requêtes
            List<List<StatementPattern>> queries = queryParser.parseQueries();

            SearchEngine mozilla = new SearchEngine(dataParser);

            // Traitement des options
            if (cmd.hasOption("Jena")) {
                // Activer la vérification avec Jena
                // Utiliser Jena comme un oracle

                // Créer un modèle vide
                Model model = ModelFactory.createDefaultModel();

                // Charger le fichier RDF dans le modèle
                FileManager.get().readModel(model, dataPath);
                int cpt = 0;
                int score = 0;
                // pour chaque requête du fichier de requêtes
                for (String query : queryParser.getStrQueries()) {

                    // Créer une requête Jena
                    Query jenaQuery = QueryFactory.create(query);

                    // Exécuter la requête sur le modèle
                    try (QueryExecution qexec = QueryExecutionFactory.create(jenaQuery, model)) {
                        ResultSet jenaResults = qexec.execSelect();

                        score += compareResults(jenaResults, mozilla.query(queries.get(cpt++)), cpt);

                    } catch (Exception e) {
                        // Gérer l'exception ici
                        e.printStackTrace();
                    }
                }
                System.out.println("Score : " + score + "/" + queryParser.getStrQueries().size());
                System.exit(0);
            }

            if (cmd.hasOption("warm")) {
                String warmPercentage = cmd.getOptionValue("warm");
                // Utiliser un échantillon de requêtes correspondant au pourcentage "X"
            }

            if (cmd.hasOption("shuffle")) {
                // Considérer une permutation aléatoire des requêtes
            }

            // Lire les chemins des fichiers et dossiers fournis en argument


            // Charger les requêtes et les données, puis effectuer le traitement
            // ...



            //le moteur de recherche va :
            // - Appeler le parser pour récupérer les données
            // - créer son  dictionnaire et Hexastore en interne





            System.out.println(mozilla.query(queries.get(3)));

        } catch (ParseException e) {
            System.err.println("Erreur lors de l'analyse des arguments de ligne de commande: " + e.getMessage());
            // Afficher l'aide ou quitter le programme
        }





    }

    public static int compareResults(ResultSet jenaResults, List<String> results2, int i) {

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


}
