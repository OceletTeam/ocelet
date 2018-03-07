/*
*  Ocelet spatial modelling language.   www.ocelet.org
*  Copyright Cirad 2010-2018
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
package fr.ocelet.runtime.relation;

import java.util.ArrayList;

/**
 * @author Mathieu Castets - Initial contribution
 */
public class EntityContainer {

	private ArrayList<OcltRole> list = new ArrayList<OcltRole>();

	public EntityContainer(OcltRole role) {
		list.add(role);
	}

	public OcltRole get(int index) {
		return list.get(index);
	}

	public void add(OcltRole role) {
		list.add(role);
	}

	public ArrayList<OcltRole> get() {
		return list;
	}

	public boolean contains(OcltRole r2) {
		return list.contains(r2);
	}

	public boolean containOnly(OcltRole r2) {

		if (list.size() == 1) {
			if (list.contains(r2))
				return true;
		}
		return false;
	}

	public boolean containList(EntityContainer ec, OcltRole r2) {

		boolean contain = true;
		for (OcltRole role : list) {

			if (!ec.contains(role)) {
				contain = false;
			}
			if (!ec.contains(r2)) {
				contain = false;
			}

		}
		return contain;
	}

	public EntityContainer newEC(EntityContainer ec, OcltRole r2) {

		EntityContainer e = new EntityContainer(r2);
		for (OcltRole role : ec.get()) {
			e.add(role);
		}
		return e;
	}

	public int size() {
		return list.size();
	}
}
