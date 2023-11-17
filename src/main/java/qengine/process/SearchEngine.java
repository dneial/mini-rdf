package qengine.process;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.eclipse.rdf4j.query.algebra.StatementPattern;
import qengine.parser.DataParser;
import qengine.structures.Dictionnary;
import qengine.structures.Hexastore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        for (StatementPattern statementPattern : query) {
            String predicate = statementPattern.getPredicateVar().getValue().stringValue();
            String object = statementPattern.getObjectVar().getValue().stringValue();

//            System.out.println("subject : " + subject);

            //POS
            List<Long> subjects =  hexastore.get(encode(predicate), encode(object));

            System.out.println("predicate : " + predicate + "\nencoding : " + encode(predicate));
            System.out.println("object : " + object+ "\nencoding : " + encode(object));

            for(Long s : subjects){
                result.add(decode(s));
            }
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
