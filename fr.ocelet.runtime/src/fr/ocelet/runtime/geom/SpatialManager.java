package fr.ocelet.runtime.geom;

import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import com.vividsolutions.jts.geom.GeometryFactory;

public class SpatialManager {
	protected static CoordinateReferenceSystem crs = null;
	protected static final GeometryFactory geomfactory = new OceletGeomFactory();

	public SpatialManager() {
	}

	public static GeometryFactory geometryFactory() {
		return geomfactory;
	}

	/**
	 * @return The coordinate system of this model, if there is one. Null if
	 *         none was defined.
	 */
	public static CoordinateReferenceSystem getCrs() {
		return crs;
	}

	/**
	 * Creates a transformation operation from the coordinate system of the
	 * model and the other coordinate system given in argument. If no coordinate
	 * system has been defined for the model yet, the one given in argument
	 * becomes the main coordinate system of the model and no MathTransform is
	 * returned (returns null).
	 * 
	 * @param dstCrs
	 *            Target Crs in textual form "EPSG:num" ex : "EPSG:4326"
	 * @return MathTransform A ready to use transformation. Or null if the model
	 *         did not have a coordinate system yet.
	 */
	public static MathTransform getTransformCrs(String epsgCode,
			String ERR_HEADER) {
		MathTransform mt = null;
		if (crs == null)
			try {
				crs = CRS.decode(epsgCode);
			} catch (NoSuchAuthorityCodeException e) {
				System.out.println(ERR_HEADER + "Unknown EPSG code : "
						+ epsgCode);
			} catch (FactoryException e) {
				System.out.println(ERR_HEADER
						+ "Failed to build the coordinate system :" + epsgCode);
				e.printStackTrace();
			}
		else
			try {
				// TODO We should check if the epsgCode given in argument is the
				// same
				// as the one of the reference crs. In such a case, no need to
				// produce a mt.

				CoordinateReferenceSystem destCRS = CRS.decode(epsgCode);
				mt = CRS.findMathTransform(crs, destCRS, true);
			} catch (NoSuchAuthorityCodeException e) {
				// e.printStackTrace();
				System.out.println(ERR_HEADER
						+ "Unrecognized coordinate system :" + epsgCode);
			} catch (FactoryException e) {
				// e.printStackTrace();
				System.out.println(ERR_HEADER
						+ "Failed to build the coordinate system " + epsgCode);
			}
		return mt;
	}

	/**
	 * Creates a transformation operation from the coordinate system of the
	 * model and the other coordinate system given in argument. If no coordinate
	 * system has been defined for the model yet, the one given in argument
	 * becomes the main coordinate system of the model and no MathTransform is
	 * returned (returns null).
	 * 
	 * @param dstCrs
	 *            Target Crs
	 * @return MathTransform A ready to use transformation. Or null if the model
	 *         did not have a coordinate system yet.
	 * @throws FactoryException
	 */
	public static MathTransform getTransformCrs(
			CoordinateReferenceSystem tgcrs, String ERR_HEADER)
			throws FactoryException {
		MathTransform mt = null;
		if (crs == null)
			crs = tgcrs;
		else
			mt = CRS.findMathTransform(crs, tgcrs, true);
		return mt;
	}

}
