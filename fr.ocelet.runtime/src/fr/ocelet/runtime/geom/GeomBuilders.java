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

import java.util.AbstractList;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;

import fr.ocelet.runtime.geom.ocltypes.Line;
import fr.ocelet.runtime.geom.ocltypes.Polygon;
import fr.ocelet.runtime.geom.ocltypes.Ring;

/**
 * Static builders of diverse geometries
 * @author Pascal Degenne - Initial contribution
 *
 */
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
