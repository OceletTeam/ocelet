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

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

import fr.ocelet.runtime.geom.SpatialManager;
import fr.ocelet.runtime.ocltypes.Group;
import fr.ocelet.runtime.ocltypes.List;

/**
 * Multiple polygonal geometry
 * 
 * @author Pascal Degenne - Initial contribution
 *
 */
@SuppressWarnings("serial")
public class MultiPolygon extends com.vividsolutions.jts.geom.MultiPolygon implements SpatialType{

	static final String ERR_HEADER = "MultiPolygon : ";

	/**
	 * @param polygons
	 *            the <code>Polygon</code>s for this <code>MultiPolygon</code>,
	 *            or <code>null</code> or an empty array to create the empty
	 *            geometry. Elements may be empty <code>Polygon</code>s, but not
	 *            <code>null</code>s. The polygons must conform to the
	 *            assertions specified in the <A
	 *            HREF="http://www.opengis.org/techno/specs.htm">OpenGIS Simple
	 *            Features Specification for SQL</A>.
	 */
	public MultiPolygon(Polygon[] polygons, GeometryFactory factory) {
		super(polygons, factory);
	}

	public MultiPolygon(com.vividsolutions.jts.geom.Polygon[] polygons,
			GeometryFactory factory) {
		super(polygons, factory);
	}

	public MultiPolygon() {
		super(null, SpatialManager.geometryFactory());
	}

	public MultiPolygon transform(MathTransform mt) {
		if (mt != null)
			try {
				return (MultiPolygon) JTS.transform(this, mt);
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
	 * Static constructor to build a MultiPolygon from a series of Polygons
	 * 
	 * @param pts
	 *            A series of Polygons
	 * @return A new initialized MultiPolygon
	 */
	public static MultiPolygon polygons(
			com.vividsolutions.jts.geom.Polygon... pts) {
		return new MultiPolygon(pts, SpatialManager.geometryFactory());
	}

	/**
	 * Static constructor to build a MultiPolygon from a Group of Polygons
	 * 
	 * @param gp
	 *            A Group of Polygons
	 * @return A new initialized MultiPolygon
	 */
	public static MultiPolygon polygons(
			Group<com.vividsolutions.jts.geom.Polygon> gp) {
		return new MultiPolygon(gp.toArray(new Polygon[gp.size()]),
				SpatialManager.geometryFactory());
	}

	/**
	 * Static constructor to build a MultiPolygon from a List of Polygons
	 * 
	 * @param lp
	 *            A List of Polygons
	 * @return A new initialized MultiPolygon
	 */
	public static MultiPolygon polygons(
			List<com.vividsolutions.jts.geom.Polygon> lp) {
		return new MultiPolygon(lp.toArray(new Polygon[lp.size()]),
				SpatialManager.geometryFactory());
	}

	/**
	 * Moves this MultiPolygon to a new position given a distance along x and y
	 * axis.
	 * 
	 * @param dx
	 *            Moving distance on X axis
	 * @param dy
	 *            Moving distance on Y axis
	 * @return A MultiPolygon moved to a new position
	 */
	public MultiPolygon move(double dx, double dy) {
		AffineTransform affineTransform = AffineTransform.getTranslateInstance(
				dx, dy);
		MathTransform mt = new AffineTransform2D(affineTransform);
		return transform(mt);
	}

	/**
	 * Rotates this MultiPolygon given an angle and the coordinates of an anchor
	 * rotation point.
	 * 
	 * @param angle
	 *            Rotation angle in radian
	 * @param anchorx
	 *            x coordinate of the anchor rotation point
	 * @param anchory
	 *            y coordinate of the anchor rotation point
	 * @return A MultiPolygon rotated around the anchor location
	 */
	public MultiPolygon rotate(double angle, double anchorx, double anchory) {
		AffineTransform affineTransform = AffineTransform.getRotateInstance(
				angle, anchorx, anchory);
		MathTransform mt = new AffineTransform2D(affineTransform);
		return transform(mt);
	}

	/**
	 * Scales this MultiPolygon by the given factors along x and y axis.
	 * 
	 * @param xfactor
	 *            Scaling factor along the x axis
	 * @param yfactor
	 *            Scaling factor along the y axis
	 * @return A MultiPolygon rotated
	 */
	public MultiPolygon scale(double xfactor, double yfactor) {
		AffineTransform affineTransform = AffineTransform.getScaleInstance(
				xfactor, yfactor);
		MathTransform mt = new AffineTransform2D(affineTransform);
		return transform(mt);
	}

	/**
	 * Gives access to every Polygon contained in this MultiPolygon into the
	 * form of a list of Polygons
	 * 
	 * @return An ordered list of Polygon
	 */
	public List<Polygon> asListOfPolygons() {
		List<Polygon> lp = new List<Polygon>();
		for (int i = 0; i < getNumGeometries(); i++) {
			lp.add((Polygon) this.getGeometryN(i));
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
