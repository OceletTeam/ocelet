package fr.ocelet.runtime.relation.impl;

import java.util.Iterator;

import fr.ocelet.runtime.relation.OcltEdge;
import fr.ocelet.runtime.relation.OcltRole;

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
