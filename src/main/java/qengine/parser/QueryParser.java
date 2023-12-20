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
    public Logger log;
    private List<List<StatementPattern>> queries;


    public QueryParser(String baseURI, String queryFile) {
        this.baseURI = baseURI;
        this.queryFile = queryFile;
        if(queryFile != null && !new File(queryFile).exists())
            throw new IllegalArgumentException("Le fichier de requêtes n'existe pas");
    }


    public List<String> getStrQueries() {
        List<String> strqueries = new ArrayList<>();
        for(List<StatementPattern> q: queries) {
            StringBuilder strquery = new StringBuilder();
            strquery.append("SELECT ?v0 WHERE {\n");
            for(StatementPattern sp: q) {
                //nt format
                strquery
                    .append("\t")
                    .append("?")
                    .append(sp.getSubjectVar().getName())
                    .append("\t<")
                    .append(sp.getPredicateVar().getValue())
                    .append(">\t<")
                    .append(sp.getObjectVar().getValue())
                    .append(">");
                if(q.indexOf(sp) != q.size()-1) strquery.append(" .\n");
            }
            strquery.append(" }");
            strqueries.add(strquery.toString());
        }
        return strqueries;
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

        if (queryFile == null || queryFile.isEmpty())
            return null;

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
//                    System.out.println(queryString.toString());
                    ParsedQuery query = sparqlParser.parseQuery(queryString.toString(), baseURI);
                    queries.add(processAQuery(query));

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

        List<String> strQueries = getStrQueries();
        
        queries = queries.stream().distinct().toList();

        System.out.println("Number of distinct queries : " + queries.size());

        //ajouter les requetes distinctes au fichier de benchmark
        Files.write(Paths.get("distinctQueries.queryset"), getStrQueries());

    }

    public void setQueries(List<List<StatementPattern>> newQueries) {
        this.queries = newQueries;
    }

    public static void main(String[] args) throws FileNotFoundException, IOException{
        QueryParser qp = new QueryParser(null, "data/queries/100/Q_4_location_nationality_gender_type_100.queryset");
        qp.parseQueries();

    }
}
