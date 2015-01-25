package fr.ocelet.runtime.geom.ocltypes;

import org.geotools.geometry.jts.JTS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateList;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;

import fr.ocelet.runtime.geom.SpatialManager;
import fr.ocelet.runtime.ocltypes.List;

/**
 * Simple Ring geometry
 * @author Pascal Degenne - Initial contribution
 *
 */
@SuppressWarnings("serial")
public class Ring extends LinearRing {

	static final String ERR_HEADER = "Ring : ";

	public Ring(CoordinateSequence points, GeometryFactory factory) {
		super(points, factory);
	}

	public Ring() {
		super(null, SpatialManager.geometryFactory());
	}

	public Ring transform(MathTransform mt) {
		if (mt != null)
			try {
				return (Ring) JTS.transform(this, mt);
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
	 * Static constructor to build a Ring from a series of Points. The Ring will
	 * automatically be closed if it's necessary.
	 * 
	 * @param pts
	 *            A series of Points
	 * @return A new initialized Ring
	 */
	@SuppressWarnings("unchecked")
	public static Ring points(com.vividsolutions.jts.geom.Point... pts) {
		CoordinateList cl = new CoordinateList();
		for (int i = 0; i < pts.length; i++)
			cl.add(pts[i].getCoordinate());
		cl.closeRing(); // Let's make sure the ring is closed.
		Ring ring = new Ring(
				new CoordinateArraySequence(cl.toCoordinateArray()),
				SpatialManager.geometryFactory());
		return ring;
	}

	/**
	 * Static constructor to build a Ring from a List of Points. The Ring will
	 * automatically be closed if it's necessary.
	 * 
	 * @param lp
	 *            A List of Points
	 * @return A new initialized Ring
	 */
	@SuppressWarnings("unchecked")
	public static Ring points(List<com.vividsolutions.jts.geom.Point> lp) {
		CoordinateList cl = new CoordinateList();
		for (int i = 0; i < lp.size(); i++)
			cl.add(lp.get(i).getCoordinate());
		cl.closeRing(); // Let's make sure the ring is closed.
		Ring ring = new Ring(
				new CoordinateArraySequence(cl.toCoordinateArray()),
				SpatialManager.geometryFactory());
		return ring;
	}

}
