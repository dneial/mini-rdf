package qengine.parser;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.helpers.AbstractRDFHandler;

import qengine.process.Logger;
import qengine.structures.Dictionnary;
import qengine.structures.Hexastore;

public final class MainRDFHandler extends AbstractRDFHandler {
	private Dictionnary dictionnary = new Dictionnary();

	private Hexastore hexastore = new Hexastore();

	@Override
	public void handleStatement(Statement st) {
		String subject = st.getSubject().toString();
		String predicate = st.getPredicate().stringValue();
		String object = st.getObject().toString();

		Logger.instance.dicoCreateLapStart();
		dictionnary.put(subject);
		dictionnary.put(predicate);
		dictionnary.put(object);
		Logger.instance.dicoCreateLapEnd();

		Logger.instance.countRdfTriple();

		Logger.instance.hexaCreateLapStart();
		hexastore.put(dictionnary.get(subject), dictionnary.get(predicate), dictionnary.get(object));
		Logger.instance.hexaCreateLapEnd();
	}

	public Hexastore getHexastore() {
		return hexastore;
	}

	public Dictionnary getDictionnary() {
		return dictionnary;
	}
}
