package qengine.process;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.rdf4j.query.algebra.StatementPattern;
import qengine.parser.DataParser;
import qengine.parser.QueryParser;
import qengine.structures.Dictionnary;
import qengine.structures.Hexastore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchEngine {
    public Dictionnary encoder;
    public Hexastore hexastore;
    public int emptyQueries = 0;

    Map<List<StatementPattern>, List<String>> results;

    public SearchEngine(){}

    public void initData(DataParser dataParser) throws IOException {
        Pair<Hexastore, Dictionnary> p = dataParser.parseData();
        this.results = new HashMap<>();
        this.hexastore = p.getLeft();
        this.encoder = p.getRight();
    }

    public List<String> query(List<StatementPattern> query){

        List<String> result = new ArrayList<>();
        List<Long> intermediate = new ArrayList<>();

        for (StatementPattern statementPattern : query) {
            String predicate = statementPattern.getPredicateVar().getValue().stringValue();
            String object = statementPattern.getObjectVar().getValue().stringValue();

            //POS
            List<Long> subjects =  hexastore.get(encode(predicate), encode(object));
            if (subjects.isEmpty()) {
                emptyQueries++;
                return result;
            }

            if (intermediate.isEmpty()) {
                intermediate.addAll(subjects);
            }
            else {
                intermediate.retainAll(subjects);
                if (intermediate.isEmpty()) {
                    emptyQueries++;
                    return result;
                }
            }
        }

        for (Long l : intermediate) {
            result.add(decode(l));
        }

        this.results.put(query, result);
        return result;
    }

    public Map<List<StatementPattern>, List<String>> queryAll(List<List<StatementPattern>> queries){
        Map<List<StatementPattern>, List<String>> result = new HashMap<>();
        this.emptyQueries = 0;
        Logger.instance.startWorkloadEvalTime();

        for (List<StatementPattern> query : queries) {
            result.put(query, query(query));
        }

        Logger.instance.stopWorkloadEvalTime();

        this.results = result;
        return result;
    }
    private Long encode(String s){
        return encoder.get(s);
    }

    private String decode(Long l){
        return encoder.get(l);
    }

    public void displayResults(){
        if (results.isEmpty()) {
            System.out.println("Empty Object");
            return;
        }
        if (results.values().stream().allMatch(List::isEmpty)) {
            System.out.println("Queries returned no results");
            return;
        }

        System.out.println(results.size() + " results");
        System.out.println("Queries :\n");
        for (Map.Entry<List<StatementPattern>, List<String>> entry : results.entrySet()) {
            System.out.println("------------\nQuery :");

            System.out.println("SELECT ?v0 WHERE {");

            for (StatementPattern statementPattern : entry.getKey()) {
                System.out.println("\t" + statementPattern.getPredicateVar().getValue() + " " + statementPattern.getObjectVar().getValue() + " .");
            }
            System.out.println("}\n");

            System.out.println("Results : " + results.get(entry.getKey()));
        }
    }

    public Map<List<StatementPattern>, List<String>> getResults() {
        return results;
    }

    public static void main(String[] args) throws IOException {
        //methode pour créer deux fichiers de benchmark:
        // un avec uniquement des requêtes distinctes
        // un avec une seule requête dupliquée assez de fois pour avoir la même taille que le premier

        SearchEngine searchEngine = new SearchEngine();
        QueryParser queryParser = new QueryParser(null, "resultats/benchmark.queryset");
        queryParser.parseQueries();
        queryParser.writeDistinctBench();
    }

}
