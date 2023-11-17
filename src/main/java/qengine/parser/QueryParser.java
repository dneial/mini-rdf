package qengine.parser;

import org.eclipse.rdf4j.query.algebra.Projection;
import org.eclipse.rdf4j.query.algebra.StatementPattern;
import org.eclipse.rdf4j.query.algebra.helpers.AbstractQueryModelVisitor;
import org.eclipse.rdf4j.query.algebra.helpers.StatementPatternCollector;
import org.eclipse.rdf4j.query.parser.ParsedQuery;
import org.eclipse.rdf4j.query.parser.sparql.SPARQLParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class QueryParser {

    private String baseURI;
    private String workingDir;
    private String queryFile;

    public QueryParser(String baseURI, String workingDir, String queryFile) {
        this.baseURI = baseURI;
        this.workingDir = workingDir;
        this.queryFile = workingDir +"/"+ queryFile;
        //TODO : vérifier que le fichier existe
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

                    // Reset le buffer de la requête en chaine vide
                    queryString.setLength(0);
                }
            }
        }
        return queries;
    }

    public static List<StatementPattern> processAQuery(ParsedQuery query) {
        List<StatementPattern> patterns = StatementPatternCollector.process(query.getTupleExpr());

        int cpt = 0;
//        for (StatementPattern pattern : patterns) {
//            System.out.println("pattern " + cpt + " : " + pattern);
//            cpt++;
//        }

        //patterns.get(0).getObjectVar().getValue());

//        System.out.println("variables to project : ");

        // Utilisation d'une classe anonyme
//        query.getTupleExpr().visit(new AbstractQueryModelVisitor<RuntimeException>() {
//
//            public void meet(Projection projection) {
//                System.out.println(projection.getProjectionElemList().getElements());
//            }
//        });
        return patterns;
    }

}
