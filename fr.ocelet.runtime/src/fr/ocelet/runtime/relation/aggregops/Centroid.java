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
 * @author Pascal Degenne, initial contribution
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
