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

import fr.ocelet.runtime.ocltypes.List;

/**
 * Generic Aggregation Operator definition . Those aggregation operators are
 * used when several candidate values are simultaneously affected to an Entity's
 * property. That situation happens when an entity is connected to several
 * others in an interaction graph and the interaction function assigns a new
 * value to one property of that entity. New values will come from every arc of
 * the graph that is connected to the entity. An aggregation operator is used to
 * avoid any affectation conflict and perform the appropriate operation in case
 * of multiple values.
 * 
 * @author Pascal Degenne, initial contribution
 *
 */
public interface AggregOperator<T, R extends List<T>> {

	/**
	 * Computes all the candidate values and produces one unique value of the same
	 * type to be used for property affectation.
	 * 
	 * @param future
	 *            Vector of candidate values.
	 * @param actualValue
	 *            The actual value that is provided in case of needed, either in the
	 *            computation or just to be able to use it as a result to leave the
	 *            actual value unchanged.
	 * @return One unique value
	 */
	public T compute(R future, T preval);
}
