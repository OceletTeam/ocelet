package fr.ocelet.runtime.geom;

import java.util.AbstractList;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;

import fr.ocelet.runtime.geom.ocltypes.Line;
import fr.ocelet.runtime.geom.ocltypes.Polygon;
import fr.ocelet.runtime.geom.ocltypes.Ring;

public class GeomBuilders {

	// --------- Line builders --------

	public static Line segment(com.vividsolutions.jts.geom.Point p1,
			com.vividsolutions.jts.geom.Point p2) {
		Coordinate[] coords = new Coordinate[2];
		coords[0] = p1.getCoordinate();
		coords[1] = p2.getCoordinate();
		CoordinateSequence cs = SpatialManager.geometryFactory()
				.getCoordinateSequenceFactory().create(coords);
		return new Line(cs, SpatialManager.geometryFactory());
	}

	public static Line path(
			AbstractList<com.vividsolutions.jts.geom.Point> cpoints) {
		Coordinate[] coords = new Coordinate[cpoints.size()];
		int px = 0;
		for (com.vividsolutions.jts.geom.Point p : cpoints) {
			coords[px] = p.getCoordinate();
		}
		CoordinateSequence cs = SpatialManager.geometryFactory()
				.getCoordinateSequenceFactory().create(coords);
		return new Line(cs, SpatialManager.geometryFactory());
	}

	// -------- Polygon builders --------

	public static Polygon closedpath(
			AbstractList<com.vividsolutions.jts.geom.Point> cpoints) {
		int pathsz = cpoints.size();
		com.vividsolutions.jts.geom.Point first = cpoints.get(0);
		com.vividsolutions.jts.geom.Point last = cpoints.get(pathsz - 1);
		if (!first.equals(last))
			pathsz = pathsz + 1;

		Coordinate[] coords = new Coordinate[pathsz];
		int px = 0;
		for (com.vividsolutions.jts.geom.Point p : cpoints) {
			coords[px] = p.getCoordinate();
			px++;
		}
		if (pathsz > cpoints.size())
			coords[px] = cpoints.get(0).getCoordinate();
		CoordinateSequence cs = SpatialManager.geometryFactory()
				.getCoordinateSequenceFactory().create(coords);
		Ring shell = new Ring(cs, SpatialManager.geometryFactory());
		return new Polygon(shell, null, SpatialManager.geometryFactory());
	}

}
