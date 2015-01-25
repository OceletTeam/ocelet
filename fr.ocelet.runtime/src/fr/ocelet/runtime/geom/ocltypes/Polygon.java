package fr.ocelet.runtime.geom.ocltypes;

import org.geotools.geometry.jts.JTS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

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
	
}
