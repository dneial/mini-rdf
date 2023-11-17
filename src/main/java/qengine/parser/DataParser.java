package qengine.parser;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
import qengine.structures.Dictionnary;
import qengine.structures.Hexastore;


import java.io.*;

public class DataParser {

    private String baseURI;
    private String workingDir;
    private String dataFile;

    public DataParser(String baseURI, String workingDir, String dataFile) {
        this.baseURI = baseURI;
        this.workingDir = workingDir;
        this.dataFile = workingDir +"/"+ dataFile;
        //TODO : vérifier que le fichier existe
    }

    public Pair<Hexastore, Dictionnary> parseData() throws FileNotFoundException, IOException {

        try (Reader dataReader = new FileReader(dataFile)) {
            // On va parser des données au format ntriples
            RDFParser rdfParser = Rio.createParser(RDFFormat.NTRIPLES);

            MainRDFHandler handler = new MainRDFHandler();
            // On utilise notre implémentation de handler
            rdfParser.setRDFHandler(handler);

            // Parsing et traitement de chaque triple par le handler
            rdfParser.parse(dataReader, baseURI);

            return new ImmutablePair<>(handler.getHexastore(), handler.getDictionnary());
        }
    }
}
