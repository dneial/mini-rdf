package qengine.parser;

import org.eclipse.rdf4j.query.algebra.StatementPattern;
import org.eclipse.rdf4j.query.algebra.helpers.StatementPatternCollector;
import org.eclipse.rdf4j.query.parser.ParsedQuery;
import org.eclipse.rdf4j.query.parser.sparql.SPARQLParser;
import qengine.process.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class QueryParser {

    private String baseURI;
    private String queryFile;
    private List<String> strQueries;
    public Logger log;
    private List<List<StatementPattern>> queries;


    public QueryParser(String baseURI, String queryFile) {
        this.baseURI = baseURI;
        this.queryFile = queryFile;
        this.strQueries = new ArrayList<>();
        if(!new File(queryFile).exists())
            throw new IllegalArgumentException("Le fichier de requêtes n'existe pas");
    }


    public List<String> getStrQueries() {
        return strQueries;
    }

    public List<List<StatementPattern>> parseQueries(String queryPath) throws FileNotFoundException, IOException {
        this.queryFile = queryPath;
        return parseQueries();
    }

    public List<List<StatementPattern>> parseQueries() throws FileNotFoundException, IOException {
        /*
         * On utilise un stream pour lire les lignes une par une, sans avoir à toutes les stocker
         * entièrement dans une collection.
         */

        List<List<StatementPattern>> queries = new ArrayList<>();
        try (Stream<String> lineStream = Files.lines(Paths.get(queryFile))) {
            SPARQLParser sparqlParser = new SPARQLParser();
            Iterator<String> lineIterator = lineStream.iterator();
            StringBuilder queryString = new StringBuilder();

            Logger.instance.startReadQueriesTime();
            while (lineIterator.hasNext())
                /*
                 * On stocke plusieurs lignes jusqu'à ce que l'une d'entre elles se termine par un '}'
                 * On considère alors que c'est la fin d'une requête
                 */
            {
                String line = lineIterator.next();
                queryString.append(line);


                if (line.trim().endsWith("}")) {
                    ParsedQuery query = sparqlParser.parseQuery(queryString.toString(), baseURI);
                    queries.add(processAQuery(query));
                    strQueries.add(queryString.toString());



                    // Reset le buffer de la requête en chaine vide
                    queryString.setLength(0);
                }
            }
            Logger.instance.stopReadQueriesTime();
        }
        Logger.instance.setNumQueries(queries.size());
        this.queries = queries;
        return queries;
    }

    public static List<StatementPattern> processAQuery(ParsedQuery query) {

        return StatementPatternCollector.process(query.getTupleExpr());
    }


    public List<List<StatementPattern>> getQueries() {
        return queries;
    }

    public void shuffle() {
        java.util.Collections.shuffle(queries);
    }

    public int getNumberOfDuplicateQueries() {
        int sizeQ = queries.size();
        int sizeSet = queries.stream().distinct().toArray().length;
        System.out.println("Number of distinct queries : " + sizeSet);
        return sizeQ - sizeSet;

    }

    //crée un benchmark avec uniquement des requêtes distinctes
    // et un autre avec une seule requête dupliquée assez de fois pour avoir la même taille que le premier
    public void writeDistinctBench() throws IOException {

        List<String> dist = strQueries.stream().distinct().toList();

        int size = dist.size();

        System.out.println("Number of distinct queries : " + size);

        //ajouter les requetes distinctes au fichier de benchmark
        Files.write(Paths.get("distinctQueries.queryset"), dist);

        //prendre la première requête et la dupliquer assez de fois pour avoir la même taille que le premier
        String first = strQueries.get(0);

        List<String> dup = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            dup.add(first);
        }
        Files.write(Paths.get("duplicateQueries.queryset"), dup);
    }
}
