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
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;

import fr.ocelet.runtime.geom.SpatialManager;
import fr.ocelet.runtime.ocltypes.List;

/**
 * Simple linear geometry
 * @author Pascal Degenne - initial contribution
 */
@SuppressWarnings("serial")
public class Line extends LineString implements SpatialType{

	static final String ERR_HEADER = "Line : ";

	/**
	 * @param points
	 *            the points of the linestring, or <code>null</code> to create
	 *            the empty geometry. Consecutive points may not be equal.
	 */
	public Line(CoordinateSequence points, GeometryFactory factory) {
		super(points, factory);
	}

	/**
	 * Empty constructor that creates an empty Line (it has no coordinate).
	 */
	public Line() {
		super(null, SpatialManager.geometryFactory());
	}

	/**
	 * Static constructor to build a line from a series of Points
	 * 
	 * @param pts
	 *            A series of Points
	 * @return A new initialized Line
	 */
	public static Line points(com.vividsolutions.jts.geom.Point... pts) {
		Coordinate[] coords = new Coordinate[pts.length];
		for (int i = 0; i < pts.length; i++)
			coords[i] = pts[i].getCoordinate();
		Line lin = new Line(new CoordinateArraySequence(coords),
				SpatialManager.geometryFactory());
		return lin;
	}

	/**
	 * Static constructor to build a line from a List of Points
	 * 
	 * @param lp
	 *            A List of Points
	 * @return A new initialized Line
	 */
	public static Line points(List<com.vividsolutions.jts.geom.Point> lp) {
		Coordinate[] coords = new Coordinate[lp.size()];
		for (int i = 0; i < lp.size(); i++)
			coords[i] = lp.get(i).getCoordinate();
		Line lin = new Line(new CoordinateArraySequence(coords),
				SpatialManager.geometryFactory());
		return lin;
	}

	public Line transform(MathTransform mt) {
		if (mt != null)
			try {
				return (Line) JTS.transform(this, mt);
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
	 * Moves this Line to a new position given a distance along x and y axis.
	 * 
	 * @param dx Moving distance on X axis
	 * @param dy Moving distance on Y axis
	 * @return A Line moved to a new position
	 */
	public Line move(double dx, double dy) {
		AffineTransform affineTransform = AffineTransform.getTranslateInstance(dx,dy);
		MathTransform mt = new AffineTransform2D(affineTransform);
		return transform(mt);
	}
	
	/**
	 * Rotates this Line given an angle and the coordinates of an anchor rotation point.
	 * 
	 * @param angle Rotation angle in radian
	 * @param anchorx x coordinate of the anchor rotation point
	 * @param anchory y coordinate of the anchor rotation point
	 * @return A Line rotated around the anchor location
	 */
	public Line rotate(double angle, double anchorx, double anchory) {
		AffineTransform affineTransform = AffineTransform.getRotateInstance(angle,anchorx,anchory);
		MathTransform mt = new AffineTransform2D(affineTransform);
		return transform(mt);
	}
	
	/**
	 * Scales this Line by the given factors along x and y axis.
	 * 
	 * @param xfactor Scaling factor along the x axis
	 * @param yfactor Scaling factor along the y axis
	 * @return A Line rotated
	 */
	public Line scale(double xfactor, double yfactor) {
		AffineTransform affineTransform = AffineTransform.getScaleInstance(xfactor, yfactor);
		MathTransform mt = new AffineTransform2D(affineTransform);
		return transform(mt);
	}

	/**
	 * Gives access to every coordinate forming this Line into the form of a list of Points
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
	
	/********************************************************/
	/***********************  UNION *************************/
	/********************************************************/
	
	/***************** POINTS *******************************/
	
	public Point pointUnion(Geometry g){
		
		Geometry geometry = this.union(g);		
		return SpatialManager.pointTransform(geometry);
	}	


	public MultiPoint multiPointUnion(Geometry g){
	
		Geometry geometry = this.union(g);		
		return SpatialManager.multiPointTransform(geometry);
	}
	
	/***************** LINES *******************************/
	
	public Line lineUnion(Geometry g){
		
		Geometry geometry = this.union(g);		
		return SpatialManager.lineTransform(geometry);
	}
	
	public MultiLine multiLineUnion(Geometry g){
		
		Geometry geometry = this.union(g);		
		return SpatialManager.multiLineTransform(geometry);
	}
	
	/***************** POLYGONS *******************************/
	
	public Polygon polygonUnion(Geometry g){
		
		Geometry geometry = this.union(g);		
		return SpatialManager.polygonTransform(geometry);
	}
	
	
	public MultiPolygon multiPolygonUnion(Geometry g){
		
		Geometry geometry = this.union(g);		
		return SpatialManager.multiPolygonTransform(geometry);
	}
	
	
	
	
	/********************************************************/
	/***************** DIFFERENCE *************************/	
	/********************************************************/
	
	/***************** POINTS *******************************/
	
	public Point pointDifference(Geometry g){
		
		Geometry geometry = this.difference(g);	
		return SpatialManager.pointTransform(geometry);
	}


	public MultiPoint multiPointDifference(Geometry g){
		
		Geometry geometry = this.difference(g);
		return SpatialManager.multiPointTransform(geometry);
	}	
		
	/***************** LINES *******************************/
	
	public Line lineDifference(Geometry g){
		
		Geometry geometry = this.difference(g);		
		return SpatialManager.lineTransform(geometry);
	}

	public MultiLine multiLineDifference(Geometry g){
	
		Geometry geometry = this.difference(g);	
		return SpatialManager.multiLineTransform(geometry);
	}
	
	/***************** POLYGONS *******************************/


	public Polygon polygonDifference(Geometry g){
	
		Geometry geometry = this.difference(g);	
		return SpatialManager.polygonTransform(geometry);
	}
	
	public MultiPoint multiPolygonDifference(Geometry g){
		
		Geometry geometry = this.difference(g);	
		return SpatialManager.multiPointTransform(geometry);
	}

	/********************************************************/
	/***************** SYM DIFFERENCE *************************/	
	/********************************************************/
	
	/***************** POINTS *******************************/
	
	public Point pointSymDifference(Geometry g){
		
		Geometry geometry = this.symDifference(g);	
		return SpatialManager.pointTransform(geometry);
	}

	public MultiPoint multiPointSymDifference(Geometry g){
		
		Geometry geometry = this.symDifference(g);
		return SpatialManager.multiPointTransform(geometry);
	}	
		
	/***************** LINES *******************************/
	
	public Line lineSymDifference(Geometry g){
		
		Geometry geometry = this.symDifference(g);		
		return SpatialManager.lineTransform(geometry);
	}

	public MultiLine multiLineSymDifference(Geometry g){
	
		Geometry geometry = this.symDifference(g);	
		return SpatialManager.multiLineTransform(geometry);
	}
	
	/***************** POLYGONS *******************************/


	public Polygon polygonSymDifference(Geometry g){
	
		Geometry geometry = this.symDifference(g);	
		return SpatialManager.polygonTransform(geometry);
	}
	
	public MultiPoint multiPolygonSymDifference(Geometry g){
		
		Geometry geometry = this.symDifference(g);	
		return SpatialManager.multiPointTransform(geometry);
	}

	

	/********************************************************/
	/***************** INTERSECTION *************************/
	/********************************************************/
	
	/***************** POINTS *******************************/
	
	public Point pointIntersection(Geometry g){
		
		Geometry geometry = this.intersection(g);	
		return SpatialManager.pointTransform(geometry);
	}

	public MultiPoint multiPointIntersection(Geometry g){
		
		Geometry geometry = this.intersection(g);
		return SpatialManager.multiPointTransform(geometry);
	}
	
	/**************** LINES *******************************/
	
	public Line lineIntersection(Geometry g){
	
		Geometry geometry = this.intersection(g);	
		return SpatialManager.lineTransform(geometry);
	}

	public MultiLine multiLineIntersection(Geometry g){
		
		Geometry geometry = this.intersection(g);	
		return SpatialManager.multiLineTransform(geometry);
	}
	
	
	/**************** POLYGONS *******************************/
	public Polygon polygonIntersection(Geometry g){
	
		Geometry geometry = this.intersection(g);	
		return SpatialManager.polygonTransform(geometry);
	}

	public MultiPolygon multiPolygonIntersection(Geometry g){
	
		Geometry geometry = this.intersection(g);
		return SpatialManager.multiPolygonTransform(geometry);
	}


	/**************** BUFFER *******************************/
	

	public Polygon polygonBuffer(Double distance){
	
		Geometry geometry = this.buffer(distance);	
		return SpatialManager.polygonTransform(geometry);
	}

	public MultiPolygon multiPolygonBuffer(Double distance){
	
		Geometry geometry = this.buffer(distance);	
		return SpatialManager.multiPolygonTransform(geometry);
	}
	
	public String getGeometricOperationType(String operation, Geometry g, Double distance){
		
		if(operation.equals("difference")){
			return this.difference(g).getClass().getSimpleName();
		}
		if(operation.equals("symDifference")){
			return this.symDifference(g).getClass().getSimpleName();
		}
		if(operation.equals("union")){
			return this.union(g).getClass().getSimpleName();
		}
		if(operation.equals("intersection")){
			return this.intersection(g).getClass().getSimpleName();
		}
		if(operation.equals("buffer")){
			return this.buffer(distance).getClass().getSimpleName();
		}
		return null;
	}
	
}
