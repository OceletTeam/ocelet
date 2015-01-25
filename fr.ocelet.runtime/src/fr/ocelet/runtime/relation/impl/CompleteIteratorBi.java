package fr.ocelet.runtime.relation.impl;

import java.util.Iterator;

import fr.ocelet.runtime.relation.OcltEdge;
import fr.ocelet.runtime.relation.OcltRole;

public class CompleteIteratorBi<E extends OcltEdge, Rg extends OcltRole, Rd extends OcltRole>
		implements Iterator<E> {

	protected DiGraph<E,Rg,Rd> bigr;
	protected Iterator<Rg> itg; // iterator on left side Roles
	protected Iterator<Rd> itd; // iterator on right side Roles
	protected Rg nextg;
	protected Rd nextd;

	public CompleteIteratorBi(DiGraph<E, Rg, Rd> big) {
		this.bigr = big;
		itg = bigr.getLeftSet().iterator();
		itd = bigr.getRightSet().iterator();
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
		else {
			if (itg.hasNext())
				nextg = itg.next();
			else
				return false;
			itd = bigr.getRightSet().iterator();
			return hasNext();
		}
		return true;
	}

	@Override
	public E next() {
		return bigr.createEdge(nextg,nextd);
	}

	@Override
	public void remove() {
	}

}
