package fr.ocelet.runtime.relation.impl;

import java.util.ArrayList;
import java.util.Iterator;

import fr.ocelet.runtime.relation.EdgeFilter;
import fr.ocelet.runtime.relation.EdgeFilteringIterator;
import fr.ocelet.runtime.relation.OcltEdge;

/**
 * An edge iterator that applies a series of edge filters.
 * 
 * @author Pascal Degenne - Initial contribution
 */
public class EdgeFilteringIteratorImpl<E extends OcltEdge, F extends EdgeFilter>
		implements EdgeFilteringIterator<E, F> {

	protected Iterator<E> edgit;
	protected final ArrayList<F> filters;
	protected E nextEdge;

	public EdgeFilteringIteratorImpl(Iterator<E> edges_source) {
		this.edgit = edges_source;
		this.filters = new ArrayList<F>();
	}

	public void addFilter(F edf) {
		filters.add(edf);
	}

	public void setSourceIterator(Iterator<E> edges_source) {
		this.edgit = edges_source;
	}

	@Override
	public boolean hasNext() {
		boolean result = false;
		while ((!result) && (edgit.hasNext())) {
			nextEdge = edgit.next();
			boolean filt = true;
			for (F ef : filters) {
				filt = filt
						& ef.filter(nextEdge.getRole(0), nextEdge.getRole(1));
				if (!filt)
					break;
			}
			result = filt;
		}
		return result;
	}

	@Override
	public E next() {
		return nextEdge;
	}

	@Override
	public void remove() {
	}
}
