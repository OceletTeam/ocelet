package fr.ocelet.runtime.geom.ocltypes;

import java.awt.geom.AffineTransform;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;

import fr.ocelet.runtime.geom.SpatialManager;
import fr.ocelet.runtime.ocltypes.Group;
import fr.ocelet.runtime.ocltypes.List;

/**
 * Multiple linear geometry
 * 
 * @author Pascal Degenne - Initial contribution
 *
 */
@SuppressWarnings("serial")
public class MultiLine extends MultiLineString {

	static final String ERR_HEADER = "MultiLine : ";

	/**
	 * @param lines
	 *            the <code>Line</code>s for this <code>MultiLine</code>, or
	 *            <code>null</code> or an empty array to create the empty
	 *            geometry. Elements may be empty <code>Line</code>s, but not
	 *            <code>null</code>s.
	 */
	public MultiLine(Line[] lines, GeometryFactory factory) {
		super(lines, factory);
	}

	public MultiLine(com.vividsolutions.jts.geom.LineString[] lines,
			GeometryFactory factory) {
		super(lines, factory);
	}

	public MultiLine() {
		super(null, SpatialManager.geometryFactory());
	}

	public MultiLine transform(MathTransform mt) {
		if (mt != null)
			try {
				return (MultiLine) JTS.transform(this, mt);
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
	 * Static constructor to build a MultiLine from a series of Lines
	 * 
	 * @param lns
	 *            A series of Lines
	 * @return A new initialized MultiLine
	 */
	public static MultiLine lines(com.vividsolutions.jts.geom.LineString... lns) {
		return new MultiLine(lns, SpatialManager.geometryFactory());
	}

	/**
	 * Static constructor to build a MultiLine from a Group of Lines
	 * 
	 * @param gl
	 *            A Group of Lines
	 * @return A new initialized MultiLine
	 */
	public static MultiLine lines(
			Group<com.vividsolutions.jts.geom.LineString> gl) {
		return new MultiLine(gl.toArray(new Line[gl.size()]),
				SpatialManager.geometryFactory());
	}

	/**
	 * Static constructor to build a MultiLine from a List of Lines
	 * 
	 * @param ll
	 *            A List of Lines
	 * @return A new initialized MultiLine
	 */
	public static MultiLine lines(
			List<com.vividsolutions.jts.geom.LineString> ll) {
		return new MultiLine(ll.toArray(new Line[ll.size()]),
				SpatialManager.geometryFactory());
	}

	/**
	 * Moves this MultiLine to a new position given a distance along x and y
	 * axis.
	 * 
	 * @param dx
	 *            Moving distance on X axis
	 * @param dy
	 *            Moving distance on Y axis
	 * @return A MultiLine moved to a new position
	 */
	public MultiLine move(double dx, double dy) {
		AffineTransform affineTransform = AffineTransform.getTranslateInstance(
				dx, dy);
		MathTransform mt = new AffineTransform2D(affineTransform);
		return transform(mt);
	}

	/**
	 * Rotates this MultiLine given an angle and the coordinates of an anchor
	 * rotation point.
	 * 
	 * @param angle
	 *            Rotation angle in radian
	 * @param anchorx
	 *            x coordinate of the anchor rotation point
	 * @param anchory
	 *            y coordinate of the anchor rotation point
	 * @return A MultiLine rotated around the anchor location
	 */
	public MultiLine rotate(double angle, double anchorx, double anchory) {
		AffineTransform affineTransform = AffineTransform.getRotateInstance(
				angle, anchorx, anchory);
		MathTransform mt = new AffineTransform2D(affineTransform);
		return transform(mt);
	}

	/**
	 * Scales this MultiLine by the given factors along x and y axis.
	 * 
	 * @param xfactor
	 *            Scaling factor along the x axis
	 * @param yfactor
	 *            Scaling factor along the y axis
	 * @return A MultiLine rotated
	 */
	public MultiLine scale(double xfactor, double yfactor) {
		AffineTransform affineTransform = AffineTransform.getScaleInstance(
				xfactor, yfactor);
		MathTransform mt = new AffineTransform2D(affineTransform);
		return transform(mt);
	}

	/**
	 * Gives access to every Line contained in this MultiLine into the form of a
	 * list of Lines
	 * 
	 * @return An ordered list of Line
	 */
	public List<Line> asListOfLines() {
		List<Line> ll = new List<Line>();
		for (int i = 0; i < getNumGeometries(); i++) {
			Line newline = new Line(
					((LineString) this.getGeometryN(i)).getCoordinateSequence(),
					SpatialManager.geometryFactory());
			if (newline != null)
				ll.add(newline);
		}
		return ll;
	}
}
