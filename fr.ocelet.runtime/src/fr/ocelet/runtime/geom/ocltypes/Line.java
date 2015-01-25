package fr.ocelet.runtime.geom.ocltypes;

import org.geotools.geometry.jts.JTS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
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
public class Line extends LineString {

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

}
