package qengine.parser;

import org.eclipse.rdf4j.query.algebra.Projection;
import org.eclipse.rdf4j.query.algebra.StatementPattern;
import org.eclipse.rdf4j.query.algebra.helpers.AbstractQueryModelVisitor;
import org.eclipse.rdf4j.query.algebra.helpers.StatementPatternCollector;
import org.eclipse.rdf4j.query.parser.ParsedQuery;
import org.eclipse.rdf4j.query.parser.sparql.SPARQLParser;
import qengine.process.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class QueryParser {

    private String baseURI;
    private String queryFile;
    private List<String> strQueries;
    public Logger log;


    public QueryParser(String baseURI, String queryFile) {
        this.baseURI = baseURI;
        this.queryFile = queryFile;
        this.strQueries = new ArrayList<>();
        //TODO : vérifier que le fichier existe
    }

    public List<String> getStrQueries() {
        return strQueries;
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
        return queries;
    }

    public static List<StatementPattern> processAQuery(ParsedQuery query) {

        return StatementPatternCollector.process(query.getTupleExpr());
    }


    public void setLogger(Logger log) {

    }
}
