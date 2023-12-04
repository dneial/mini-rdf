package qengine.program;

import org.apache.commons.cli.*;
import qengine.process.Logger;

public class Main {
    public static void main(String[] args) throws Exception {

        CommandLineParser parser = new DefaultParser();
        Options options = getOptions();

        try {
            CommandLine cmd = parser.parse(options, args);

            String queriesPath = cmd.getOptionValue("queries");
            String dataPath = cmd.getOptionValue("data");

            if (cmd.hasOption("output")) {
                // Activer le logger
                Logger.instance.setActive(true);
                String outputPath = cmd.getOptionValue("output");
                Logger.instance.setQueriesPath(queriesPath);
                Logger.instance.setDataPath(dataPath);
                Logger.instance.setOutputPath(outputPath);
                Logger.instance.startTime();
            }
            RDFEngine rdfEngine = new RDFEngine(dataPath, queriesPath);
            rdfEngine.load();


            // Traitement des options
            if (cmd.hasOption("Jena")) {
                rdfEngine.runJenaValidation();
                System.exit(0);
            }

            if (cmd.hasOption("warm")) {

                // Utiliser un échantillon de requêtes correspondant au pourcentage "X"
                String warmPercentage = cmd.getOptionValue("warm");

                // Vérifier que le pourcentage est valide
                try {
                    int percentage = Integer.parseInt(warmPercentage);
                    if (percentage < 0 || percentage > 100) {
                        System.err.println("Erreur : le pourcentage doit être compris entre 0 et 100.");
                        System.exit(1);
                    }

                   // System.out.println("Warming up avec " + warmPercentage + "% des requêtes");
                    rdfEngine.warmup(percentage);

                } catch (NumberFormatException e) {
                    System.err.println("Erreur : le pourcentage doit être un nombre entier.");
                    System.exit(1);
                }
            }

            if (cmd.hasOption("shuffle")) {
                // Considérer une permutation aléatoire des requêtes
                rdfEngine.shuffle();
            }


            if (!cmd.hasOption("Jena")) {
                // Exécuter les requêtes sur notre moteur RDF si pas d'option Jena
                rdfEngine.run();
            }

            Logger.instance.stopTotalTime();
            Logger.instance.dump();
            if(cmd.hasOption("export_results")){
                String exportPath = cmd.getOptionValue("export_results");
                rdfEngine.dumpResults(exportPath);
            }

        } catch (ParseException e) {
            System.err.println("Erreur lors de l'analyse des arguments de ligne de commande: " + e.getMessage());
            // Afficher l'aide ou quitter le programme
        }

    }

    private static Options getOptions() {
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
        options.addOption("export_results", true, "Enregistrer les résultats des requêtes dans un fichier CSV");
        return options;
    }


}
