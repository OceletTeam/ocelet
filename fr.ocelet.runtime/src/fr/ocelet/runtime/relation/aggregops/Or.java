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

package fr.ocelet.runtime.relation.aggregops;

import java.util.Iterator;

import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.relation.AggregOperator;

/**
 * Returns the computation of a logical OR on all the values contained in the
 * argument vector. This AggregOperator can only be used with boolean
 * properties.
 * 
 * @see AggregOperator
 * @author Pascal Degenne - initial contribution
 */
public class Or implements AggregOperator<Boolean, List<Boolean>> {

	/**
	 * Computes all the candidate values and produces one unique value of the same
	 * type to be used for property affectation. In this case the logical OR of all
	 * values is returned.
	 * 
	 * @param future
	 *            Vector of candidate values.
	 * @return The logical OR of all the values contained in the argument vector.
	 */
	public Boolean compute(List<Boolean> future, Boolean preval) {

		Boolean result = future.get(0);
		if (future.size() > 1) {
			for (Iterator<Boolean> it = (Iterator<Boolean>) future.iterator(); it.hasNext();) {
				boolean val = it.next();
				result = result | val;
			}
		}
		return result;
	}
}
