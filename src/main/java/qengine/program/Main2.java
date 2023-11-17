package qengine.program;

import org.eclipse.rdf4j.query.algebra.StatementPattern;
import qengine.parser.QueryParser;
import qengine.process.SearchEngine;
import qengine.parser.DataParser;

import javax.management.Query;
import java.sql.Statement;
import java.util.List;

public class Main2 {
    public static void main(String[] args) throws Exception {

        DataParser dataParser = new DataParser(null, "data", "100K.nt");
        QueryParser queryParser = new QueryParser(null, "data", "sample_query.queryset");

        //le moteur de recherche va :
        // - Appeler le parser pour récupérer les données
        // - créer son  dictionnaire et Hexastore en interne
        SearchEngine mozilla = new SearchEngine(dataParser);

        //print hexastore
//        System.out.println(mozilla.hexastore);


        List<List<StatementPattern>> queries = queryParser.parseQueries();
        System.out.println(mozilla.queryAll(queries));
/*
        List<String> reponse = mozilla.query(queries.get(2));

        System.out.println("Query 1 : " + queries.get(2));
        System.out.println("response : "+ reponse.size() + " elements" );

        for( String s : reponse){
            System.out.println(s);
        };*/

        //methode pour une liste de requetes
        //mozilla.query(queries);


        //on va pouvoir lui faire des requetes

    }
}
