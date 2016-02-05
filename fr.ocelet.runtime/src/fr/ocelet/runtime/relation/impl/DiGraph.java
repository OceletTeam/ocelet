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
import java.util.Collection;
import java.util.Iterator;

import fr.ocelet.runtime.relation.DiGraphInterface;
import fr.ocelet.runtime.relation.EdgeFilter;
import fr.ocelet.runtime.relation.EdgeFilteringIterator;
import fr.ocelet.runtime.relation.OcltEdge;
import fr.ocelet.runtime.relation.OcltRole;
import fr.ocelet.runtime.relation.RoleSet;

/**
 * Implementation of a DiGraph that keeps everything in memory.
 * 
 * @author Pascal Degenne, Initial contribution
 */
public abstract class DiGraph<E extends OcltEdge, R1 extends OcltRole, R2 extends OcltRole>
		implements DiGraphInterface<E, R1, R2> {

	protected ArrayList<E> edges;
	protected ArrayList<E> removed;
	protected Iterator<E> filteredIterator;

	public DiGraph() {
		edges = new ArrayList<E>();
		removed = new ArrayList<E>();
		filteredIterator = null;
	}

	protected void addEdge(E newedge) {
		edges.add(newedge);
	}

	protected abstract RoleSet<R1> getLeftSet();

	protected abstract RoleSet<R2> getRightSet();

	@Override
	public void beginTransaction() {
		cleanGraph();
		for (R1 r : getLeftSet())
			r.tbegin();
		for (R2 r : getRightSet())
			r.tbegin();
	}

	@Override
	public void endTransaction() {
		for (R1 r : getLeftSet())
			r.tcommit();
		for (R2 r : getRightSet())
			r.tcommit();
		cleanGraph();
		filteredIterator = null;
	}

	@Override
	public void abortTransaction() {
		for (R1 r : getLeftSet())
			r.tabort();
		for (R2 r : getRightSet())
			r.tabort();
	}

	public void connect() {
		for (E edge : this)
			addEdge(edge);
		cleanGraph();
		filteredIterator = null;
	}

	public void disconnect() {
		for (E edge : this)
			disconnect(edge);
		cleanGraph();
		filteredIterator = null;
	}

	@Override
	public void disconnect(E edge) {
		removed.add(edge);
	}

	@Override
	public void disconnect(Iterable<E> edges) {
		for (E e : edges)
			removed.add(e);
	}

	@Override
	public Iterator<E> iterator() {
		if (filteredIterator != null)
			return filteredIterator;
		cleanGraph();
		return edges.iterator();
	}

	@Override
	public int size() {
		return edges.size() - removed.size();
	}

	public DiGraph<E, R1, R2> getComplete() {
		CompleteIteratorBi<E, R1, R2> cit = new CompleteIteratorBi<E, R1, R2>(
				this);
		filteredIterator = cit;
		return this;
	}

	public void addFilter(EdgeFilter<R1, R2> ef) {
		if ((filteredIterator == null)
				|| !(filteredIterator instanceof EdgeFilteringIterator))
			filteredIterator = new EdgeFilteringIteratorImpl(iterator());
		((EdgeFilteringIterator) filteredIterator).addFilter(ef);
	}

	protected void cleanGraph() {
		if (getLeftSet().hasRemovedRoles() || getRightSet().hasRemovedRoles()) {
			Collection<R1> remRolesg = getLeftSet().getRecentlyRemovedRoles();
			Collection<R2> remRolesd = getRightSet().getRecentlyRemovedRoles();
			for (E ed : edges) {
				OcltRole org = ed.getRole(0);
				OcltRole ord = ed.getRole(1);
				if (remRolesg.contains(org) || remRolesd.contains(ord))
					ed.disconnect();
			}
			getLeftSet().resetRemovedRoles();
			getRightSet().resetRemovedRoles();
		}
		if (!removed.isEmpty()) {
			for (E ed : removed)
				edges.remove(ed);
			removed.clear();
		}

	}

	public String toString() {
		return "Interaction graph (" + this.getClass().getSimpleName()
				+ ") contains " + size() + " edges.";
	}
}
