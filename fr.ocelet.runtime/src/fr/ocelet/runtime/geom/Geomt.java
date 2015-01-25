package fr.ocelet.runtime.geom;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import fr.ocelet.runtime.geom.ocltypes.Line;
import fr.ocelet.runtime.geom.ocltypes.Point;

public class Geomt {

	public Geomt() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Geomt gt = new Geomt();
		gt.tLine();
		gt.tPoint();
	}
	
	public void tPoint() {
		Point p = new Point();
		p.setX(23.4);
		p.setY(56.678);
		System.out.println("Our point is "+p);
	}

	public void tLine() {
		Line l1 = new Line(SpatialManager
				.geometryFactory()
				.getCoordinateSequenceFactory()
				.create(new Coordinate[] { new Coordinate(1, 0),
						new Coordinate(1, 2), new Coordinate(2, 2),
						new Coordinate(2, 0) }), SpatialManager.geomfactory);

		Line l2 = new Line(SpatialManager
				.geometryFactory()
				.getCoordinateSequenceFactory()
				.create(new Coordinate[] { new Coordinate(0, 1),
						new Coordinate(3, 1) }), SpatialManager.geomfactory);

		Geometry g = l2.intersection(l1);
		System.out.println("g is a " + g.getClass().getName());
		System.out.println("g=" + g);
	}

}
