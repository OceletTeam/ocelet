package fr.ocelet.runtime.relation;

import java.util.Iterator;

public interface EdgeFilteringIterator<E extends OcltEdge, F extends EdgeFilter>
		extends Iterator<E> {

	public void addFilter(F edf);
	public void setSourceIterator(Iterator<E> edges_source);
}
