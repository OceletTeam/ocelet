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

package fr.ocelet.runtime.relation.aggregops;

import java.util.Iterator;

import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.relation.AggregOperator;

/**
 * Returns The maximum value taken from the argument vector. This AggregOperator
 * should only be used with number properties (real and int in Ocelet).
 * 
 * @see AggregOperator
 * @author Pascal Degenne, initial contribution
 */
public class Max<T extends Comparable<T>> implements AggregOperator<T,List<T>> {

	/**
	 * Computes all the candidate values and produces one unique value of the
	 * same type to be used for property affectation. In this case the maximum
	 * value is returned.
	 * 
	 * @param future
	 *            Vector of candidate values.
	 * @return The maximum value found in the argument future Vector.
	 */
	public T compute(List<T> future, T preval) {

		T choice = null;
		if (future.size() > 1) {
			for (Iterator<T> it = future.iterator(); it.hasNext();) {
				T val = it.next();
				if ((choice == null) || choice.compareTo(val) < 0)
					choice = val;
			}
		} else
			choice = future.get(0);
		return choice;
	}

	
}
