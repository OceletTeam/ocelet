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

import java.util.HashMap;
import java.util.Iterator;

import fr.ocelet.runtime.ocltypes.List;

/**
 * @author Mathieu Castets - Initial contribution
 */
public class CellValues {

	public CellValues() {
		values = new HashMap<String, List<Double>>();
	}

	public void set(String name) {
		List<Double> aList = new List<Double>();
		values.put(name, aList);
	}

	public void add(String name, Double value) {
		values.get(name).add(value);
	}

	public void clear(String name) {
		values.get(name).clear();
	}

	public List<Double> getValues(String name) {
		return values.get(name);
	}

	public void clearAll() {
		String name;
		for (Iterator<String> iterator = values.keySet().iterator(); iterator.hasNext(); values.get(name).clear())
			name = (String) iterator.next();

	}

	@Override
	public String toString() {
		String s = "";
		for (String name : values.keySet()) {
			s += values.get(name).toString();
		}
		return s;
	}

	private HashMap<String, List<Double>> values;
}
