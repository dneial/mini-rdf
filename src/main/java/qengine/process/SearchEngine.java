package qengine.process;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.rdf4j.query.algebra.StatementPattern;
import qengine.parser.DataParser;
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


    public SearchEngine(DataParser dataParser) throws IOException {
        Pair<Hexastore, Dictionnary> p = dataParser.parseData();

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
            if (subjects.isEmpty()) return result;

            if (intermediate.isEmpty()) {
                intermediate.addAll(subjects);
            }
            else {
                intermediate.retainAll(subjects);
                if (intermediate.isEmpty()) return result;
            }
        }

        for (Long l : intermediate) {
            result.add(decode(l));
        }

        return result;
    }

    public Map<List<StatementPattern>, List<String>> queryAll(List<List<StatementPattern>> queries){
        Map<List<StatementPattern>, List<String>> result = new HashMap<>();

        for (List<StatementPattern> query : queries) {
            result.put(query, query(query));
        }

        return result;
    }
    private Long encode(String s){
        return encoder.get(s);
    }

    private String decode(Long l){
        return encoder.get(l);
    }
}
