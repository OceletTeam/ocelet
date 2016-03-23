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

package fr.ocelet.runtime.geom;

import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ocelet.runtime.geom.ocltypes.Line;
import fr.ocelet.runtime.geom.ocltypes.MultiLine;
import fr.ocelet.runtime.geom.ocltypes.MultiPoint;
import fr.ocelet.runtime.geom.ocltypes.MultiPolygon;
import fr.ocelet.runtime.geom.ocltypes.Point;
import fr.ocelet.runtime.geom.ocltypes.Polygon;

/**
 * A class that keep track of the coordinate reference system
 * in use in an Ocelet projet and that also provides the crs
 * transformation functions.
 * 
 * @author Pascal Degenne
 *
 */
public class SpatialManager {
	protected static CoordinateReferenceSystem crs = null;
	protected static final GeometryFactory geomfactory = new OceletGeomFactory();

	public SpatialManager() {
	}

	public static GeometryFactory geometryFactory() {
		return geomfactory;
	}

	/**
	 * @return The coordinate system of this model, if there is one. Null if
	 *         none was defined.
	 */
	public static CoordinateReferenceSystem getCrs() {
		return crs;
	}

	/**
	 * Creates a transformation operation from the coordinate system of the
	 * model and the other coordinate system given in argument. If no coordinate
	 * system has been defined for the model yet, the one given in argument
	 * becomes the main coordinate system of the model and no MathTransform is
	 * returned (returns null).
	 * 
	 * @param dstCrs
	 *            Target Crs in textual form "EPSG:num" ex : "EPSG:4326"
	 * @return MathTransform A ready to use transformation. Or null if the model
	 *         did not have a coordinate system yet.
	 */
	public static MathTransform getTransformCrs(String epsgCode,
			String ERR_HEADER) {
		MathTransform mt = null;
		if (crs == null)
			try {
				crs = CRS.decode(epsgCode);
			} catch (NoSuchAuthorityCodeException e) {
				System.out.println(ERR_HEADER + "Unknown EPSG code : "
						+ epsgCode);
			} catch (FactoryException e) {
				System.out.println(ERR_HEADER
						+ "Failed to build the coordinate system :" + epsgCode);
				e.printStackTrace();
			}
		else
			try {
				// TODO We should check if the epsgCode given in argument is the
				// same
				// as the one of the reference crs. In such a case, no need to
				// produce a mt.

				CoordinateReferenceSystem destCRS = CRS.decode(epsgCode);
				mt = CRS.findMathTransform(crs, destCRS, true);
			} catch (NoSuchAuthorityCodeException e) {
				// e.printStackTrace();
				System.out.println(ERR_HEADER
						+ "Unrecognized coordinate system :" + epsgCode);
			} catch (FactoryException e) {
				// e.printStackTrace();
				System.out.println(ERR_HEADER
						+ "Failed to build the coordinate system " + epsgCode);
			}
		return mt;
	}

	/**
	 * Creates a transformation operation from the coordinate system of the
	 * model and the other coordinate system given in argument. If no coordinate
	 * system has been defined for the model yet, the one given in argument
	 * becomes the main coordinate system of the model and no MathTransform is
	 * returned (returns null).
	 * 
	 * @param dstCrs
	 *            Target Crs
	 * @return MathTransform A ready to use transformation. Or null if the model
	 *         did not have a coordinate system yet.
	 * @throws FactoryException
	 */
	public static MathTransform getTransformCrs(
			CoordinateReferenceSystem tgcrs, String ERR_HEADER)
			throws FactoryException {
		MathTransform mt = null;
		if (crs == null)
			crs = tgcrs;
		else
			mt = CRS.findMathTransform(crs, tgcrs, true);
		return mt;
	}
	
public static Line lineTransform(Geometry geometry){
		
		if(geometry instanceof Line){
			return (Line)geometry;
		}
		return null;
	}
    public static MultiLine multiLineTransform(Geometry geometry){
		
		if(geometry instanceof MultiLine){
			return (MultiLine)geometry;
		}
		if(geometry instanceof Line){
			Line[] ls = new Line[1];
			ls[0] = (Line)geometry;
			MultiLine ml = new MultiLine(ls, SpatialManager.geometryFactory());
			return ml;
		}
		return null;
	}	
    
public static Point pointTransform(Geometry geometry){
		
		if(geometry instanceof Point){
			return (Point)geometry;
		}
		return null;
	}
    public static MultiPoint multiPointTransform(Geometry geometry){
		
		if(geometry instanceof MultiPoint){
			return (MultiPoint)geometry;
		}
		if(geometry instanceof Point){
			Point[] ps = new Point[1];
			ps[0] = (Point)geometry;
			MultiPoint mp = new MultiPoint(ps, SpatialManager.geometryFactory());
			return mp;
		}
		return null;
	}
    
public static Polygon polygonTransform(Geometry geometry){
		
		if(geometry instanceof Polygon){
			return (Polygon)geometry;
		}
		return null;
	}
    public static MultiPolygon multiPolygonTransform(Geometry geometry){
		
		if(geometry instanceof MultiPolygon){
			return (MultiPolygon)geometry;
		}
		if(geometry instanceof Polygon){
			Polygon[] ps = new Polygon[1];
			ps[0] = (Polygon)geometry;
			MultiPolygon mp = new MultiPolygon(ps, SpatialManager.geometryFactory());
			return mp;
		}
		return null;
	}

}
