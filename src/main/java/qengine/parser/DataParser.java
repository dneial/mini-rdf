package qengine.parser;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
import qengine.process.Logger;
import qengine.structures.Dictionnary;
import qengine.structures.Hexastore;


import java.io.*;

public class DataParser {

    private String baseURI;
    private String dataFile;

    public DataParser(String baseURI, String dataFile) {
        this.baseURI = baseURI;
        this.dataFile = dataFile;
        //TODO : vérifier que le fichier existe
    }

    public DataParser(String baseURI, String dataFile, Logger log) {
        this.baseURI = baseURI;
        this.dataFile = dataFile;
        //TODO : vérifier que le fichier existe
    }



    public Pair<Hexastore, Dictionnary> parseData() throws FileNotFoundException, IOException {

        try (Reader dataReader = new FileReader(dataFile)) {
            Logger.instance.startReadDataTime();
            // On va parser des données au format ntriples
            RDFParser rdfParser = Rio.createParser(RDFFormat.NTRIPLES);

            MainRDFHandler handler = new MainRDFHandler();
            // On utilise notre implémentation de handler
            rdfParser.setRDFHandler(handler);

            // Parsing et traitement de chaque triple par le handler
            rdfParser.parse(dataReader, baseURI);

            Logger.instance.stopReadDataTime();
            return new ImmutablePair<>(handler.getHexastore(), handler.getDictionnary());
        }
    }
}
