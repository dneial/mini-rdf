package qengine.program;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.helpers.AbstractRDFHandler;

import qengine.structures.Dictionnary;
import qengine.structures.Hexastore;

/**
 * Le RDFHandler intervient lors du parsing de données et permet d'appliquer un traitement pour chaque élément lu par le parseur.
 * 
 * <p>
 * Ce qui servira surtout dans le programme est la méthode {@link #handleStatement(Statement)} qui va permettre de traiter chaque triple lu.
 * </p>
 * <p>
 * À adapter/réécrire selon vos traitements.
 * </p>
 */
public final class MainRDFHandler extends AbstractRDFHandler {
	private Dictionnary dictionnary = new Dictionnary();
	private Hexastore hexastore = new Hexastore();

	@Override
	public void handleStatement(Statement st) {
		String subject = st.getSubject().toString();
		String predicate = st.getPredicate().stringValue();
		String object = st.getObject().toString();

		dictionnary.put(subject);
		dictionnary.put(predicate);
		dictionnary.put(object);

		hexastore.put(dictionnary.get(subject), dictionnary.get(predicate), dictionnary.get(object));
	}

	public Hexastore getHexastore() {
		return hexastore;
	}
}
