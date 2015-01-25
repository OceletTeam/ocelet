package fr.ocelet.runtime.geom;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.PrecisionModel;

import fr.ocelet.runtime.geom.ocltypes.Line;
import fr.ocelet.runtime.geom.ocltypes.MultiLine;
import fr.ocelet.runtime.geom.ocltypes.MultiPoint;
import fr.ocelet.runtime.geom.ocltypes.MultiPolygon;
import fr.ocelet.runtime.geom.ocltypes.Point;
import fr.ocelet.runtime.geom.ocltypes.Polygon;
import fr.ocelet.runtime.geom.ocltypes.Ring;

@SuppressWarnings("serial")
public class OceletGeomFactory extends GeometryFactory {

	public OceletGeomFactory(PrecisionModel precisionModel, int SRID,
			CoordinateSequenceFactory coordinateSequenceFactory) {
		super(precisionModel, SRID, coordinateSequenceFactory);
	}

	public OceletGeomFactory(CoordinateSequenceFactory coordinateSequenceFactory) {
		super(coordinateSequenceFactory);
	}

	public OceletGeomFactory(PrecisionModel precisionModel) {
		super(precisionModel);
	}

	public OceletGeomFactory(PrecisionModel precisionModel, int SRID) {
		super(precisionModel, SRID);
	}

	public OceletGeomFactory() {
	    this(new PrecisionModel(), 0);
	  }
	
	/**
	 * Creates a Point using the given CoordinateSequence; a null or empty
	 * CoordinateSequence will create an empty Point.
	 */
	@Override
	public com.vividsolutions.jts.geom.Point createPoint(
			CoordinateSequence coordinates) {
		return new Point(coordinates, this);
	}

	/**
	 * Creates a Line using the given CoordinateSequence; a null or empty
	 * CoordinateSequence will create an empty Line. Consecutive points must not
	 * be equal.
	 * 
	 * @param coordinates
	 *            a CoordinateSequence possibly empty, or null
	 */
	@Override
	public LineString createLineString(CoordinateSequence coordinates) {
		return new Line(coordinates, this);
	}

	/**
	 * Creates a Ring using the given CoordinateSequence; a null or empty
	 * CoordinateSequence will create an empty Ring. The points must form a
	 * closed and simple linestring. Consecutive points must not be equal.
	 * 
	 * @param coordinates
	 *            a CoordinateSequence possibly empty, or null
	 */
	@Override
	public LinearRing createLinearRing(CoordinateSequence coordinates) {
		return new Ring(coordinates, this);
	}

	/**
	 * Constructs a <code>Polygon</code> with the given exterior boundary and
	 * interior boundaries.
	 * 
	 * @param shell
	 *            the outer boundary of the new <code>Polygon</code>, or
	 *            <code>null</code> or an empty <code>LinearRing</code> if the
	 *            empty geometry is to be created.
	 * @param holes
	 *            the inner boundaries of the new <code>Polygon</code>, or
	 *            <code>null</code> or empty <code>LinearRing</code> s if the
	 *            empty geometry is to be created.
	 */
	@Override
	public com.vividsolutions.jts.geom.Polygon createPolygon(LinearRing shell,
			LinearRing[] holes) {
		// (Ring) provoque un class cast exception !
		return new Polygon(shell, holes, this);
	}

	/**
	 * Creates a MultiPoint using the given Points. A null or empty array will
	 * create an empty MultiPoint.
	 * 
	 * @param coordinates
	 *            an array (without null elements), or an empty array, or
	 *            <code>null</code>
	 * @return a MultiPoint object
	 */
	@Override
	public com.vividsolutions.jts.geom.MultiPoint createMultiPoint(
			com.vividsolutions.jts.geom.Point[] point) {
		return new MultiPoint(point, this);
	}

	/**
	 * Ocelet geom version
	 */
	public MultiPoint createMultiPoint(Point[] point) {
		return new MultiPoint(point, this);
	}

	/**
	 * Creates a MultiPoint using the given CoordinateSequence. A null or empty
	 * CoordinateSequence will create an empty MultiPoint.
	 * 
	 * @param coordinates
	 *            a CoordinateSequence (possibly empty), or <code>null</code>
	 * @return a MultiPoint object
	 */
	@Override
	public com.vividsolutions.jts.geom.MultiPoint createMultiPoint(
			CoordinateSequence coordinates) {
		if (coordinates == null) {
			return (MultiPoint) createMultiPoint(new Point[0]);
		}
		Point[] points = new Point[coordinates.size()];
		for (int i = 0; i < coordinates.size(); i++) {
			points[i] = (Point) createPoint(coordinates.getCoordinate(i));
		}
		return (MultiPoint) createMultiPoint(points);
	}

	/**
	 * Creates a MultiLine using the given LineStrings; a null or empty array
	 * will create an empty MultiLine.
	 * 
	 * @param lineStrings
	 *            LineStrings, each of which may be empty but not null
	 */
	@Override
	public MultiLineString createMultiLineString(LineString[] lineStrings) {
		return new MultiLine(lineStrings, this);
	}

	/**
	 * Creates a MultiPolygon using the given Polygons; a null or empty array
	 * will create an empty Polygon. The polygons must conform to the assertions
	 * specified in the <A
	 * HREF="http://www.opengis.org/techno/specs.htm">OpenGIS Simple Features
	 * Specification for SQL</A>.
	 * 
	 * @param polygons
	 *            Polygons, each of which may be empty but not null
	 */
	@Override
	public com.vividsolutions.jts.geom.MultiPolygon createMultiPolygon(
			com.vividsolutions.jts.geom.Polygon[] polygons) {
		return new MultiPolygon(polygons, this);
	}

}
