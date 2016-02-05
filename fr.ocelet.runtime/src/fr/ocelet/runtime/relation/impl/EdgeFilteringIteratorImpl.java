/*
*  Ocelet spatial modelling language.   www.ocelet.org
*  Copyright Cirad 2010-2016
*
*  This software is a domain specific programming language dedicated to writing
*  spatially explicit models and performing spatial dynamics simulations.
*
*  This software is governed by the CeCILL license under French law and
*  abiding by the rules of distribution of free software.  You can  use,
*  modify and/ or redistribute the software under the terms of the CeCILL
*  license as circulated by CEA, CNRS and INRIA at the following URL
*  "http://www.cecill.info".
*  As a counterpart to the access to the source code and  rights to copy,
*  modify and redistribute granted by the license, users are provided only
*  with a limited warranty  and the software's author,  the holder of the
*  economic rights,  and the successive licensors  have only limited
*  liability.
*  The fact that you are presently reading this means that you have had
*  knowledge of the CeCILL license and that you accept its terms.
*/

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
