package fr.ocelet.runtime.geom.ocltypes;

import java.awt.geom.AffineTransform;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ocelet.runtime.geom.SpatialManager;
import fr.ocelet.runtime.ocltypes.Group;
import fr.ocelet.runtime.ocltypes.List;

/**
 * Multiple polygonal geometry
 * @author Pascal Degenne - Initial contribution
 *
 */
@SuppressWarnings("serial")
public class MultiPolygon extends com.vividsolutions.jts.geom.MultiPolygon {

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
	public static MultiPolygon polygons(com.vividsolutions.jts.geom.Polygon... pts) {
		return new MultiPolygon(pts, SpatialManager.geometryFactory());
	}

	/**
	 * Static constructor to build a MultiPolygon from a Group of Polygons
	 * 
	 * @param gp
	 *            A Group of Polygons
	 * @return A new initialized MultiPolygon
	 */
	public static MultiPolygon polygons(Group<com.vividsolutions.jts.geom.Polygon> gp) {
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
	public static MultiPolygon polygons(List<com.vividsolutions.jts.geom.Polygon> lp) {
		return new MultiPolygon(lp.toArray(new Polygon[lp.size()]),
				SpatialManager.geometryFactory());
	}

	/**
	 * Moves this MultiPolygon to a new position given a distance along x and y axis.
	 * 
	 * @param dx Moving distance on X axis
	 * @param dy Moving distance on Y axis
	 * @return A MultiPolygon moved to a new position
	 */
	public MultiPolygon move(double dx, double dy) {
		AffineTransform affineTransform = AffineTransform.getTranslateInstance(dx,dy);
		MathTransform mt = new AffineTransform2D(affineTransform);
		return transform(mt);
	}
	
	/**
	 * Rotates this MultiPolygon given an angle and the coordinates of an anchor rotation point.
	 * 
	 * @param angle Rotation angle in radian
	 * @param anchorx x coordinate of the anchor rotation point
	 * @param anchory y coordinate of the anchor rotation point
	 * @return A MultiPolygon rotated around the anchor location
	 */
	public MultiPolygon rotate(double angle, double anchorx, double anchory) {
		AffineTransform affineTransform = AffineTransform.getRotateInstance(angle,anchorx,anchory);
		MathTransform mt = new AffineTransform2D(affineTransform);
		return transform(mt);
	}
	
	/**
	 * Scales this MultiPolygon by the given factors along x and y axis.
	 * 
	 * @param xfactor Scaling factor along the x axis
	 * @param yfactor Scaling factor along the y axis
	 * @return A MultiPolygon rotated
	 */
	public MultiPolygon scale(double xfactor, double yfactor) {
		AffineTransform affineTransform = AffineTransform.getScaleInstance(xfactor, yfactor);
		MathTransform mt = new AffineTransform2D(affineTransform);
		return transform(mt);
	}
	
	
}
