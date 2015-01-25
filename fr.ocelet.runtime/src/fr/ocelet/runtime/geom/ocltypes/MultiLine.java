package fr.ocelet.runtime.geom.ocltypes;

import org.geotools.geometry.jts.JTS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiLineString;

import fr.ocelet.runtime.geom.SpatialManager;
import fr.ocelet.runtime.ocltypes.Group;
import fr.ocelet.runtime.ocltypes.List;

/**
 * Multiple linear geometry
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

	public MultiLine(com.vividsolutions.jts.geom.LineString[] lines, GeometryFactory factory) {
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
	public static MultiLine lines(Group<com.vividsolutions.jts.geom.LineString> gl) {
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
	public static MultiLine lines(List<com.vividsolutions.jts.geom.LineString> ll) {
		return new MultiLine(ll.toArray(new Line[ll.size()]),
				SpatialManager.geometryFactory());
	}

	
}
