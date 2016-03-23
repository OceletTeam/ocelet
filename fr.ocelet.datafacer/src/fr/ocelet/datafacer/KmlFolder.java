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

package fr.ocelet.datafacer;

import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import de.micromata.opengis.kml.v_2_2_0.AltitudeMode;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Link;
import de.micromata.opengis.kml.v_2_2_0.Location;
import de.micromata.opengis.kml.v_2_2_0.Model;
import de.micromata.opengis.kml.v_2_2_0.MultiGeometry;
import de.micromata.opengis.kml.v_2_2_0.Orientation;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Scale;
import fr.ocelet.runtime.geom.SpatialManager;
import fr.ocelet.runtime.geom.ocltypes.Line;
import fr.ocelet.runtime.geom.ocltypes.MultiLine;
import fr.ocelet.runtime.geom.ocltypes.MultiPoint;
import fr.ocelet.runtime.geom.ocltypes.MultiPolygon;
import fr.ocelet.runtime.geom.ocltypes.Point;
import fr.ocelet.runtime.geom.ocltypes.Polygon;

/**
 * Create and fills a folder in a KML file
 * 
 * @author Pascal Degenne - Initial contribution
 * 
 */
public class KmlFolder {
	protected Folder fold;
	static final String ERR_HEADER = "Kml Export: ";

	public KmlFolder(Document doc, String label, String beginDate,
			String endDate) {
		this.fold = doc.createAndAddFolder();
		fold.withName(label).createAndSetTimeSpan().withBegin(beginDate)
				.withEnd(endDate);
	}

	public KmlFolder(Document doc, String label) {
		this.fold = doc.createAndAddFolder();
		fold.withName(label);
	}

	/**
	 * Adds a KML Extruded Label (see
	 * https://kml-samples.googlecode.com/svn/trunk
	 * /interactive/index.html#./Point_Placemarks/Point_Placemarks.Extruded.kml)
	 * 
	 * @param xpos
	 *            Latitude or X position, units depending on the CRS used in the model
	 * @param ypos
	 *            Longitude or Y position, units depending on the CRS used in the model
	 * @param height
	 *            Height at which the label should be displayed
	 * @param beginDate
	 *            Begining date of the timespan
	 * @param endDate
	 *            End date of the timespan
	 * @param name
	 *            Title (always displayed)
	 * @param description
	 *            Description (only displayed on mouse click on the label)
	 */
	public void addLabel(double xpos, double ypos, double height,
			String beginDate, String endDate, String name, String description,
			String styleName) {
		Placemark placemark = fold.createAndAddPlacemark().withStyleUrl(
				"#" + styleName);
		placemark.createAndSetTimeSpan().withBegin(beginDate).withEnd(endDate);
		de.micromata.opengis.kml.v_2_2_0.Point ls = placemark
				.createAndSetPoint();
		ls.setExtrude(true);
		ls.setAltitudeMode(AltitudeMode.RELATIVE_TO_GROUND);

		Point pos = new Point();
		pos.setX(xpos);
		pos.setY(ypos);
		MathTransform mt = SpatialManager.getTransformCrs("EPSG:4326",
				ERR_HEADER);
		
		ls.addToCoordinates(pos.transform(mt).getX(), pos.transform(mt).getY(), height);
		placemark.setName(name);
		placemark.setDescription(description);
	}

	/**
	 * Adds a 3D model (most probably made with Sketchup)
	 * 
	 * @param xpos
	 *            Latitude or X position, units depending on the CRS used in the model
	 * @param ypos
	 *            Longitude or Y position, units depending on the CRS used in the model
	 * @param orientation
	 *            Orientation
	 * @param scale
	 *            Scale
	 * @param beginDate
	 *            Begining date of the timespan
	 * @param endDate
	 *            End date of the timespan
	 * @param daefile
	 *            Collada (.dae) file
	 */
	public void add3DModel(double xpos, double ypos, double orientation,
			double scale, String beginDate, String endDate, String daefile) {

		Placemark placemark = fold.createAndAddPlacemark();
		placemark.createAndSetTimeSpan().withBegin(beginDate).withEnd(endDate);
		Model model = placemark.createAndSetModel();
		model.setAltitudeMode(AltitudeMode.RELATIVE_TO_GROUND);

		Point pos = new Point();
		pos.setX(xpos);
		pos.setY(ypos);
		MathTransform mt = SpatialManager.getTransformCrs("EPSG:4326",
				ERR_HEADER);

		Location loc = new Location().withLatitude(pos.transform(mt).getY())
				.withLongitude(pos.transform(mt).getX()).withAltitude(0.0);
		model.setLocation(loc);
		model.setScale(new Scale().withX(scale).withY(scale).withZ(scale));
		model.setLink(new Link().withHref(daefile));
		model.setOrientation(new Orientation().withHeading(orientation));
	}

	/**
	 * Add a placemark with a geometry object.The geometry can be a Point, a
	 * Line, a Polygon or any Multi-geometry. Points will be represented by an
	 * icon and linear or surface objects will be drawn.
	 * 
	 * @param label
	 *            The title of the folder that will be created for this
	 *            ShpRecord
	 * @param beginDate
	 *            Begining date of the timespan
	 * @param endDate
	 *            End date of the timespan
	 * @param geom
	 *            Geometry object to be drawn
	 * @param height
	 *            Height of the feature to draw. If > 0 the feature will be
	 *            shown extruded to the given height (relative to the ground
	 *            level). If <= 0 the feature will be drawn flat on the ground.
	 */
	public void addGeometry(String label, String beginDate, String endDate,
			Geometry geom, String styleName, double height) {
		Placemark placemark = fold.createAndAddPlacemark().withStyleUrl(
				"#" + styleName);
		placemark.setName(label);
		placemark.createAndSetTimeSpan().withBegin(beginDate).withEnd(endDate);
		
		CoordinateReferenceSystem crs = null;
		try {
			crs = CRS.decode("EPSG:4326");
		} catch (NoSuchAuthorityCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(crs.getName().equals(SpatialManager.getCrs().getName())){
			if (geom instanceof Point)
				addPoint(placemark, ((Point) geom), height);
			else if (geom instanceof Line)
				addLine(placemark, ((Line) geom), height);
			else if (geom instanceof Polygon)
				addPolygon(placemark, ((Polygon) geom), height);
			else if (geom instanceof MultiPoint)
				addMultiPoint(placemark, ((MultiPoint) geom), height);
			else if (geom instanceof MultiLine)
				addMultiLine(placemark, ((MultiLine) geom), height);
			else if (geom instanceof MultiPolygon)
				addMultiPolygon(placemark, ((MultiPolygon) geom),
						height);
		}else{
			MathTransform mt = SpatialManager.getTransformCrs("EPSG:4326",
					ERR_HEADER);
			if (geom instanceof Point)
				addPoint(placemark, ((Point) geom).transform(mt), height);
			else if (geom instanceof Line)
				addLine(placemark, ((Line) geom).transform(mt), height);
			else if (geom instanceof Polygon)
				addPolygon(placemark, ((Polygon) geom).transform(mt), height);
			else if (geom instanceof MultiPoint)
				addMultiPoint(placemark, ((MultiPoint) geom).transform(mt), height);
			else if (geom instanceof MultiLine)
				addMultiLine(placemark, ((MultiLine) geom).transform(mt), height);
			else if (geom instanceof MultiPolygon)
				addMultiPolygon(placemark, ((MultiPolygon) geom).transform(mt),
						height);
		}
	}

	private void addPoint(Placemark pm, Point point, double height) {
		de.micromata.opengis.kml.v_2_2_0.Point kmlpoint = pm
				.createAndSetPoint();
		fillPoint(kmlpoint, point, height);
	}

	private void fillPoint(de.micromata.opengis.kml.v_2_2_0.Point kmlpoint,
			Point point, double height) {
		Coordinate pos = point.getCoordinate();
		if (height > 0.0) {
			kmlpoint.setExtrude(true);
			kmlpoint.setAltitudeMode(AltitudeMode.RELATIVE_TO_GROUND);
			kmlpoint.addToCoordinates(pos.x, pos.y, height);
		} else
			kmlpoint.addToCoordinates(pos.x, pos.y);
	}

	private void addLine(Placemark pm, Line line, double height) {
		de.micromata.opengis.kml.v_2_2_0.LineString kmlline = pm
				.createAndSetLineString();
		fillLine(kmlline, line, height);
	}

	private void fillLine(de.micromata.opengis.kml.v_2_2_0.LineString kmlline,
			Line line, double height) {
		if (height > 0.0) {
			kmlline.setExtrude(true);
			kmlline.setAltitudeMode(AltitudeMode.RELATIVE_TO_GROUND);
			for (Coordinate pos : line.getCoordinates()) {
				kmlline.addToCoordinates(pos.x, pos.y, height);
			}
		} else {
			for (Coordinate pos : line.getCoordinates()) {
				kmlline.addToCoordinates(pos.x, pos.y);
			}
		}
	}

	private void addPolygon(Placemark pm, Polygon poly, double height) {
		de.micromata.opengis.kml.v_2_2_0.Polygon kmlpoly = pm
				.createAndSetPolygon();
		fillPolygon(kmlpoly, poly, height);
	}

	private void fillPolygon(de.micromata.opengis.kml.v_2_2_0.Polygon kmlpoly,
			Polygon poly, double height) {

		// Shell ring
		de.micromata.opengis.kml.v_2_2_0.LinearRing kmlring = kmlpoly
				.createAndSetOuterBoundaryIs().createAndSetLinearRing();
		if (height > 0.0) {
			kmlpoly.setExtrude(true);
			kmlpoly.setAltitudeMode(AltitudeMode.RELATIVE_TO_GROUND);
			for (Coordinate pos : poly.getExteriorRing().getCoordinates()) {
				kmlring.addToCoordinates(pos.x, pos.y, height);
			}
		} else {
			kmlpoly.setTessellate(true);
			for (Coordinate pos : poly.getExteriorRing().getCoordinates()) {
				kmlring.addToCoordinates(pos.x, pos.y);
			}
		}

		// Holes
		for (int hi = 0; hi < poly.getNumInteriorRing(); hi++) {
			de.micromata.opengis.kml.v_2_2_0.LinearRing kmlhole = kmlpoly
					.createAndAddInnerBoundaryIs().createAndSetLinearRing();
			if (height > 0.0) {
				kmlpoly.setExtrude(true);
				kmlpoly.setAltitudeMode(AltitudeMode.RELATIVE_TO_GROUND);
				for (Coordinate pos : poly.getInteriorRingN(hi)
						.getCoordinates()) {
					kmlhole.addToCoordinates(pos.x, pos.y, height);
				}
			} else {
				kmlpoly.setTessellate(true);
				for (Coordinate pos : poly.getInteriorRingN(hi)
						.getCoordinates()) {
					kmlhole.addToCoordinates(pos.x, pos.y);
				}
				;
			}
		}
	}

	private void addMultiPoint(Placemark pm, MultiPoint mpoint, double height) {
		int ng = mpoint.getNumGeometries();
		MultiGeometry mg = pm.createAndSetMultiGeometry();
		for (int gx = 0; gx < ng; gx++) {
			de.micromata.opengis.kml.v_2_2_0.Point kmlpoint = mg
					.createAndAddPoint();
			fillPoint(kmlpoint, (Point) mpoint.getGeometryN(gx), height);
		}
	}

	private void addMultiLine(Placemark pm, MultiLine mline, double height) {
		int ng = mline.getNumGeometries();
		MultiGeometry mg = pm.createAndSetMultiGeometry();
		for (int gx = 0; gx < ng; gx++) {
			de.micromata.opengis.kml.v_2_2_0.LineString kmlline = mg
					.createAndAddLineString();
			fillLine(kmlline, (Line) mline.getGeometryN(gx), height);
		}
	}

	private void addMultiPolygon(Placemark pm, MultiPolygon mpoly, double height) {
		int ng = mpoly.getNumGeometries();
		MultiGeometry mg = pm.createAndSetMultiGeometry();
		for (int gx = 0; gx < ng; gx++) {
			de.micromata.opengis.kml.v_2_2_0.Polygon kmlpoly = mg
					.createAndAddPolygon();
			fillPolygon(kmlpoly, (Polygon) mpoly.getGeometryN(gx), height);
		}
	}
}
