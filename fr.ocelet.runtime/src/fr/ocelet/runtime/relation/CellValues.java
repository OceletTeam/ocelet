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
		
		iValues = new HashMap<Integer, List<Double>>();
	}

	
	
	public void set(int band) {
		List<Double> aList = new List<Double>();
		iValues.put(band, aList);
	}

	
	
	public void add(int band, Double value) {
		iValues.get(band).add(value);
	}


	
	public void clear(int band) {
		iValues.get(band).clear();
	}

	
	
	public List<Double> getValues(int band) {
		return iValues.get(band);
	}

	
	
	public void clearAll() {
		for(Integer k : iValues.keySet()) {
			iValues.get(k).clear();
		}
		

	}

	

	
	private HashMap<Integer, List<Double>> iValues;
}
