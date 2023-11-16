package qengine.program;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.helpers.AbstractRDFHandler;

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
	private Map<String, Long> dictionnary = new HashMap<String, Long>();
	private Long entries = 0L;
	private Hexastore hexastore = new Hexastore();

	@Override
	public void handleStatement(Statement st) {
		String subject = st.getSubject().toString();
		String predicate = st.getPredicate().stringValue();
		String object = st.getObject().toString();

		if(!dictionnary.containsKey(subject)) {
			dictionnary.put(subject, entries);
			entries++;
		}
		if(!dictionnary.containsKey(predicate)) {
			dictionnary.put(predicate, entries);
			entries++;
		}
		if(!dictionnary.containsKey(object)) {
			dictionnary.put(object, entries);
			entries++;
		}
		hexastore.put(dictionnary.get(subject), dictionnary.get(predicate), dictionnary.get(object));
	}

	public Map<String, Long> getDictionnary() {
		return dictionnary;
	}
}
