package fr.ocelet.runtime.geom.ocltypes;

import java.awt.geom.AffineTransform;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
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
public class Polygon extends com.vividsolutions.jts.geom.Polygon {

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
	
}
