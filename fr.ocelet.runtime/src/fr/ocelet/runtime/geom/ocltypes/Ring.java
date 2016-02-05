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

package fr.ocelet.runtime.geom.ocltypes;

import java.awt.geom.AffineTransform;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateList;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;

import fr.ocelet.runtime.geom.SpatialManager;
import fr.ocelet.runtime.ocltypes.List;

/**
 * Simple Ring geometry
 * @author Pascal Degenne - Initial contribution
 *
 */
@SuppressWarnings("serial")
public class Ring extends LinearRing {

	static final String ERR_HEADER = "Ring : ";

	public Ring(CoordinateSequence points, GeometryFactory factory) {
		super(points, factory);
	}

	public Ring() {
		super(null, SpatialManager.geometryFactory());
	}

	public Ring transform(MathTransform mt) {
		if (mt != null)
			try {
				return (Ring) JTS.transform(this, mt);
			} catch (MismatchedDimensionException e) {
				System.out
						.println(ERR_HEADER
								+ "Mismatched dimensions when trying to transform coordinates to a new coordinates system.");
			} catch (TransformException e) {
				System.out
						.println(ERR_HEADER
								+ "Transformation error when trying to transform coordinates to a new coordinates system.");
			}
		return this;
	}

	/**
	 * Static constructor to build a Ring from a series of Points. The Ring will
	 * automatically be closed if it's necessary.
	 * 
	 * @param pts
	 *            A series of Points
	 * @return A new initialized Ring
	 */
	@SuppressWarnings("unchecked")
	public static Ring points(com.vividsolutions.jts.geom.Point... pts) {
		CoordinateList cl = new CoordinateList();
		for (int i = 0; i < pts.length; i++)
			cl.add(pts[i].getCoordinate());
		cl.closeRing(); // Let's make sure the ring is closed.
		Ring ring = new Ring(
				new CoordinateArraySequence(cl.toCoordinateArray()),
				SpatialManager.geometryFactory());
		return ring;
	}

	/**
	 * Static constructor to build a Ring from a List of Points. The Ring will
	 * automatically be closed if it's necessary.
	 * 
	 * @param lp
	 *            A List of Points
	 * @return A new initialized Ring
	 */
	@SuppressWarnings("unchecked")
	public static Ring points(List<com.vividsolutions.jts.geom.Point> lp) {
		CoordinateList cl = new CoordinateList();
		for (int i = 0; i < lp.size(); i++)
			cl.add(lp.get(i).getCoordinate());
		cl.closeRing(); // Let's make sure the ring is closed.
		Ring ring = new Ring(
				new CoordinateArraySequence(cl.toCoordinateArray()),
				SpatialManager.geometryFactory());
		return ring;
	}

	/**
	 * Moves this Ring to a new position given a distance along x and y axis.
	 * 
	 * @param dx Moving distance on X axis
	 * @param dy Moving distance on Y axis
	 * @return A Ring moved to a new position
	 */
	public Ring move(double dx, double dy) {
		AffineTransform affineTransform = AffineTransform.getTranslateInstance(dx,dy);
		MathTransform mt = new AffineTransform2D(affineTransform);
		return transform(mt);
	}
	
	/**
	 * Rotates this Ring given an angle and the coordinates of an anchor rotation point.
	 * 
	 * @param angle Rotation angle in radian
	 * @param anchorx x coordinate of the anchor rotation point
	 * @param anchory y coordinate of the anchor rotation point
	 * @return A Ring rotated around the anchor location
	 */
	public Ring rotate(double angle, double anchorx, double anchory) {
		AffineTransform affineTransform = AffineTransform.getRotateInstance(angle,anchorx,anchory);
		MathTransform mt = new AffineTransform2D(affineTransform);
		return transform(mt);
	}
	
	/**
	 * Scales this Ring by the given factors along x and y axis.
	 * 
	 * @param xfactor Scaling factor along the x axis
	 * @param yfactor Scaling factor along the y axis
	 * @return A Ring rotated
	 */
	public Ring scale(double xfactor, double yfactor) {
		AffineTransform affineTransform = AffineTransform.getScaleInstance(xfactor, yfactor);
		MathTransform mt = new AffineTransform2D(affineTransform);
		return transform(mt);
	}
	
	/**
	 * Gives access to every coordinate forming this Ring into the form of a list of Points
	 * @return An ordered list of Point
	 */
	public List<Point> asListOfPoints() {
		List<Point> lp = new List<Point>();
		for (Coordinate coord : this.getCoordinates()) {
		  Point newp = Point.xy(coord.x,coord.y);
		  lp.add(newp);
		}
		return lp;
	}

}
