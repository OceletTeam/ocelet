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

import fr.ocelet.runtime.geom.SpatialManager;
import fr.ocelet.runtime.geom.ocltypes.MultiPoint;
import fr.ocelet.runtime.geom.ocltypes.Point;
import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.relation.AggregOperator;

/**
 * Returns the centroid of a series of Point. This AggregOperator can
 * only be used with Point properties.
 * 
 * @see AggregOperator
 * @author Pascal Degenne - initial contribution
 */
public class Centroid implements AggregOperator<Point, List<Point>> {

	/**
	 * Computes all the candidate values and produces one unique value of the
	 * same type to be used for property affectation. In this case the centroid
	 * all Point values is returned.
	 * 
	 * @param future
	 *            Vector of candidate values.
	 * @return The logical centroid of all the Point values contained in the argument
	 *         vector.
	 */
	public Point compute(List<Point> future, Point preval) {
		Point[] ap = new Point[future.size()];
		MultiPoint mp = new MultiPoint((Point[])future.toArray(ap),SpatialManager.geometryFactory());
		return (Point) mp.getCentroid();
	}
}
