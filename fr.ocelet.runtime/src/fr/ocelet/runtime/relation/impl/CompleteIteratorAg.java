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

import java.util.Iterator;

import fr.ocelet.runtime.relation.OcltEdge;
import fr.ocelet.runtime.relation.OcltRole;

/**
 * Iterator that give access toall the edges of a complete interaction graph.
 * @author Pascal Degenne - Initial contribution
 *
 * @param <E>
 * @param <Ro>
 */
public class CompleteIteratorAg<E extends OcltEdge, Ro extends OcltRole>
		implements Iterator<E> {

	protected AutoGraph<E,Ro> agr;
	protected Iterator<Ro> itg; // iterator on left side Roles
	protected Iterator<Ro> itd; // iterator on right side Roles
	protected Ro nextg;
	protected Ro nextd;

	public CompleteIteratorAg(AutoGraph<E, Ro> ag) {
		this.agr = ag;
		itg = agr.getLeftSet().iterator();
		itd = agr.getRightSet().iterator();
	}

	@Override
	public boolean hasNext() {
		if (nextg == null) {
			if (itg.hasNext())
				nextg = itg.next();
			else
				return false;
		}
		if (itd.hasNext())
			nextd = itd.next();
		if (nextg == nextd) {
			if (itg.hasNext())
				nextg = itg.next();
			else
				return false;
			itd = agr.getRightSet().iterator();
			return hasNext();
		}
		return true;
	}

	@Override
	public E next() {
		return agr.createEdge(nextg,nextd);
	}

	@Override
	public void remove() {
	}

}
