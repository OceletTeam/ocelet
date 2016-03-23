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
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;

import fr.ocelet.runtime.geom.SpatialManager;
import fr.ocelet.runtime.ocltypes.List;

/**
 * Simple polygonal geometry
 * @author Pascal Degenne - Initial contribution
 *
 */
@SuppressWarnings("serial")
public class Polygon extends com.vividsolutions.jts.geom.Polygon implements SpatialType{

	static final String ERR_HEADER = "Polygon : ";

	/**
	 * Empty constructor that creates an empty Line (it has no coordinate).
	 */
	public Polygon() {
		super(null, null, SpatialManager.geometryFactory());
	}

	public Polygon(Ring shell, Ring[] holes, GeometryFactory factory) {
		super(shell, holes, factory);
	}

	public Polygon(LinearRing shell, LinearRing[] holes, GeometryFactory factory) {
		super(shell, holes, factory);
	}

	public Polygon transform(MathTransform mt) {
		if (mt != null)
			try {
				return (Polygon) JTS.transform(this, mt);
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
	 * Static constructor to build a Polygon from a series of Points. The
	 * Polygon will automatically be closed if it's necessary.
	 * 
	 * @param pts
	 *            A series of Points
	 * @return A new initialized Polygon
	 */
	public static Polygon points(com.vividsolutions.jts.geom.Point... pts) {
		Ring shell = Ring.points(pts);
		return new Polygon(shell, null, SpatialManager.geometryFactory());
	}

	/**
	 * Static constructor to build a Polygon from a List of Points. The Polygon
	 * will automatically be closed if it's necessary.
	 * 
	 * @param lp
	 *            A List of Points
	 * @return A new initialized Polygon
	 */
	public static Polygon points(List<com.vividsolutions.jts.geom.Point> lp) {
		Ring shell = Ring.points(lp);
		return new Polygon(shell, null, SpatialManager.geometryFactory());
	}

	
	/**
	 * Moves this Polygon to a new position given a distance along x and y axis.
	 * 
	 * @param dx Moving distance on X axis
	 * @param dy Moving distance on Y axis
	 * @return A Polygon moved to a new position
	 */
	public Polygon move(double dx, double dy) {
		AffineTransform affineTransform = AffineTransform.getTranslateInstance(dx,dy);
		MathTransform mt = new AffineTransform2D(affineTransform);
		return transform(mt);
	}
	
	/**
	 * Rotates this Polygon given an angle and the coordinates of an anchor rotation point.
	 * 
	 * @param angle Rotation angle in radian
	 * @param anchorx x coordinate of the anchor rotation point
	 * @param anchory y coordinate of the anchor rotation point
	 * @return A Polygon rotated around the anchor location
	 */
	public Polygon rotate(double angle, double anchorx, double anchory) {
		AffineTransform affineTransform = AffineTransform.getRotateInstance(angle,anchorx,anchory);
		MathTransform mt = new AffineTransform2D(affineTransform);
		return transform(mt);
	}
	
	/**
	 * Scales this Polygon by the given factors along x and y axis.
	 * 
	 * @param xfactor Scaling factor along the x axis
	 * @param yfactor Scaling factor along the y axis
	 * @return A Polygon rotated
	 */
	public Polygon scale(double xfactor, double yfactor) {
		AffineTransform affineTransform = AffineTransform.getScaleInstance(xfactor, yfactor);
		MathTransform mt = new AffineTransform2D(affineTransform);
		return transform(mt);
	}

	/**
	 * Gives access to every coordinate forming this Polygon's shell into the form of a list of Points
	 * @return An ordered list of Point
	 */
	public List<Point> asListOfPoints() {
		List<Point> lp = new List<Point>();
		for (Coordinate coord : this.shell.getCoordinates()) {
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
