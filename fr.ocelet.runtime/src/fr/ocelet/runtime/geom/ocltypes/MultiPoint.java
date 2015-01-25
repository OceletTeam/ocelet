package fr.ocelet.runtime.geom.ocltypes;

import org.geotools.geometry.jts.JTS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ocelet.runtime.geom.SpatialManager;
import fr.ocelet.runtime.ocltypes.Group;
import fr.ocelet.runtime.ocltypes.List;

/**
 * Multiple punctual geometry
 * @author Pascal Degenne - Initial contribution
 *
 */
@SuppressWarnings("serial")
public class MultiPoint extends com.vividsolutions.jts.geom.MultiPoint {

	static final String ERR_HEADER = "MultiPoint : ";

	/**
	 * @param points
	 *            the <code>Point</code>s for this <code>MultiPoint</code> , or
	 *            <code>null</code> or an empty array to create the empty
	 *            geometry. Elements may be empty <code>Point</code>s, but not
	 *            <code>null</code>s.
	 */
	public MultiPoint(Point[] points, GeometryFactory factory) {
		super(points, factory);
	}

	/**
	 * @param points
	 *            the <code>Point</code>s for this <code>MultiPoint</code> , or
	 *            <code>null</code> or an empty array to create the empty
	 *            geometry. Elements may be empty <code>Point</code>s, but not
	 *            <code>null</code>s.
	 */
	public MultiPoint(com.vividsolutions.jts.geom.Point[] points,
			GeometryFactory factory) {
		super(points, factory);
	}

	public MultiPoint() {
		super(null, SpatialManager.geometryFactory());
	}

	/**
	 * Static constructor to build a MultiPoint from a series of Points
	 * 
	 * @param pts
	 *            A series of Points
	 * @return A new initialized MultiPoint
	 */
	public static MultiPoint points(com.vividsolutions.jts.geom.Point... pts) {
		return new MultiPoint(pts, SpatialManager.geometryFactory());
	}

	/**
	 * Static constructor to build a MultiPoint from a Group of Points
	 * 
	 * @param gp
	 *            A Group of Points
	 * @return A new initialized MultiPoint
	 */
	public static MultiPoint points(Group<com.vividsolutions.jts.geom.Point> gp) {
		return new MultiPoint(gp.toArray(new Point[gp.size()]),
				SpatialManager.geometryFactory());
	}

	/**
	 * Static constructor to build a MultiPoint from a List of Points
	 * 
	 * @param lp
	 *            A List of Points
	 * @return A new initialized MultiPoint
	 */
	public static MultiPoint points(List<com.vividsolutions.jts.geom.Point> lp) {
		return new MultiPoint(lp.toArray(new Point[lp.size()]),
				SpatialManager.geometryFactory());
	}

	public MultiPoint transform(MathTransform mt) {
		if (mt != null)
			try {
				return (MultiPoint) JTS.transform(this, mt);
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
