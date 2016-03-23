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
import com.vividsolutions.jts.geom.CoordinateSequenceComparator;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.util.Assert;

import fr.ocelet.runtime.geom.SpatialManager;

/**
 * Simple punctual geometry
 * @author Pascal Degenne - Initial contribution
 *
 */
@SuppressWarnings("serial")
public class Point extends com.vividsolutions.jts.geom.Point implements SpatialType{

	static final String ERR_HEADER = "Point : ";
	protected CoordinateSequence coordinates;

	/**
	 * Constructor used by the GeometryFactory
	 * 
	 * @param coordinates
	 *            contains the single coordinate on which to base this
	 *            <code>Point</code> , or <code>null</code> to create the empty
	 *            geometry.
	 */
	public Point(CoordinateSequence coordinates, GeometryFactory factory) {
		super(coordinates, factory);
		init(coordinates);
	}

	public Point() {
		super(SpatialManager.geometryFactory().getCoordinateSequenceFactory()
				.create(new Coordinate[] { new Coordinate(0, 0) }),
				SpatialManager.geometryFactory());
		init(null);
	}

	/**
	 * Static simple constructor
	 * @param xpos
	 * @param ypos
	 * @return A new initialized Point
	 */
	public static Point xy(Double xpos, Double ypos) {
		Point p = new Point();
		p.setX(xpos);
		p.setY(ypos);
		return p;
	}

	/**
	 * Static simple constructor
	 * @param xpos
	 * @param ypos
	 * @param zpos
	 * @return A new initialized Point
	 */
	public static Point xyz(Double xpos, Double ypos, Double zpos) {
		Point p = new Point();
		p.setX(xpos);
		p.setY(ypos);
		p.setZ(zpos);
		return p;
	}

	
	protected void init(CoordinateSequence coordinates) {
		if (coordinates == null) {
			coordinates = getFactory().getCoordinateSequenceFactory().create(
					new Coordinate[] { new Coordinate(0, 0) });
		}
		Assert.isTrue(coordinates.size() <= 1);
		this.coordinates = coordinates;
	}

	public void setX(double x) {
		coordinates.setOrdinate(0, CoordinateSequence.X, x);
	}

	public void setY(double y) {
		coordinates.setOrdinate(0, CoordinateSequence.Y, y);
	}

	public void setZ(double z) {
		coordinates.setOrdinate(0, CoordinateSequence.Z, z);
	}

	@Override
	public Coordinate getCoordinate() {
		return coordinates.size() != 0 ? coordinates.getCoordinate(0) : null;
	}

	@Override
	protected Envelope computeEnvelopeInternal() {
		if (isEmpty()) {
			return new Envelope();
		}
		Envelope env = new Envelope();
		env.expandToInclude(coordinates.getX(0), coordinates.getY(0));
		return env;
	}

	/**
	 * Creates and returns a full copy of this {@link Point} object. (including
	 * all coordinates contained by it).
	 * 
	 * @return a clone of this instance
	 */
	@Override
	public Object clone() {
		Point p = (Point) super.clone();
		p.coordinates = (CoordinateSequence) coordinates.clone();
		return p;// return the clone
	}

	@Override
	protected int compareToSameClass(Object other,
			CoordinateSequenceComparator comp) {
		Point point = (Point) other;
		return comp.compare(this.coordinates, point.coordinates);
	}

	@Override
	public CoordinateSequence getCoordinateSequence() {
		return coordinates;
	}

	public Point transform(MathTransform mt) {
		if (mt != null)
			try {
				return (Point) JTS.transform(this, mt);
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
	 * Moves this Point to a new position given a distance along x and y axis.
	 * 
	 * @param dx Moving distance on X axis
	 * @param dy Moving distance on Y axis
	 * @return A Point moved to a new position
	 */
	public Point move(double dx, double dy) {
		AffineTransform affineTransform = AffineTransform.getTranslateInstance(dx,dy);
		MathTransform mt = new AffineTransform2D(affineTransform);
		return transform(mt);
	}
	
	/**
	 * Rotates this Point given an angle and the coordinates of an anchor rotation point.
	 * 
	 * @param angle Rotation angle in radian
	 * @param anchorx x coordinate of the anchor rotation point
	 * @param anchory y coordinate of the anchor rotation point
	 * @return A Point rotated around the anchor location
	 */
	public Point rotate(double angle, double anchorx, double anchory) {
		AffineTransform affineTransform = AffineTransform.getRotateInstance(angle,anchorx,anchory);
		MathTransform mt = new AffineTransform2D(affineTransform);
		return transform(mt);
	}
	
	/**
	 * Scales this Point by the given factors along x and y axis.
	 * 
	 * @param xfactor Scaling factor along the x axis
	 * @param yfactor Scaling factor along the y axis
	 * @return A Point rotated
	 */
	public Point scale(double xfactor, double yfactor) {
		AffineTransform affineTransform = AffineTransform.getScaleInstance(xfactor, yfactor);
		MathTransform mt = new AffineTransform2D(affineTransform);
		return transform(mt);
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
