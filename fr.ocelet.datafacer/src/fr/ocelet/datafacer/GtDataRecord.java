package fr.ocelet.datafacer;

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.operation.MathTransform;

import com.vividsolutions.jts.geom.Geometry;
import fr.ocelet.runtime.geom.ocltypes.Line;
import fr.ocelet.runtime.geom.ocltypes.MultiLine;
import fr.ocelet.runtime.geom.ocltypes.MultiPoint;
import fr.ocelet.runtime.geom.ocltypes.MultiPolygon;
import fr.ocelet.runtime.geom.ocltypes.Point;
import fr.ocelet.runtime.geom.ocltypes.Polygon;
import fr.ocelet.runtime.geom.ocltypes.Ring;

/**
 * Represents one record from a GeoTools DataSource or DataStore. Such a record
 * contains a geometry attribute and a series a other attributes that can be of
 * type String, boolean, int or double.
 * 
 * @author Pascal Degenne, Initial contribution
 */
public abstract class GtDataRecord implements InputDataRecord, OutputDataRecord {

	protected SimpleFeature feature;

	public GtDataRecord() {
	}

	public GtDataRecord(SimpleFeature sf) {
		feature = sf;
	}

	@Override
	public SimpleFeature getFeature() {
		return feature;
	}

	/**
	 * Sets the value of one attribute in this record.
	 * 
	 * @param atrName
	 *            Name of the attribute to set. If it is a geometry attributen
	 *            the name is not used.
	 * @param value
	 *            Attribute value
	 */
	@Override
	public void setAttribute(String atrName, Object value) {
		if (value instanceof Geometry)
			feature.setDefaultGeometry(value);
		else
			feature.setAttribute(atrName, value);
	}

	@Override
	public Integer readInteger(int colNumber) {
		Long lv;
		try {
			lv = ((Number) feature.getAttribute(colNumber)).longValue();
			if ((lv > Integer.MAX_VALUE) || (lv < Integer.MIN_VALUE))
				System.out
						.println("Warning : attempt to convert a Long (64bit) value into an Integer (32) and the result will be truncated.");
		} catch (NullPointerException e) {
			lv = 0L;
		}
		return lv.intValue();
	}

	@Override
	public Long readLong(int colNumber) {
		try {
			return ((Number) feature.getAttribute(colNumber)).longValue();
		} catch (NullPointerException e) {
			return 0L;
		}
	}

	@Override
	public String readString(int colNumber) {
		try {
			return (String) feature.getAttribute(colNumber);
		} catch (NullPointerException e) {
			return "no data";
		}
	}

	@Override
	public Double readDouble(int colNumber) {
		try {
			return ((Number) feature.getAttribute(colNumber)).doubleValue();
		} catch (NullPointerException e) {
			return 0.0;
		}
	}

	@Override
	public Boolean readBoolean(int colNumber) {
		try {
			return ((Boolean) feature.getAttribute(colNumber)).booleanValue();
		} catch (NullPointerException e) {
			return false;
		}
	}

	@Override
	public Integer readInteger(String colName) {
		try {
			Long lv = ((Number) feature.getAttribute(colName)).longValue();
			if ((lv > Integer.MAX_VALUE) || (lv < Integer.MIN_VALUE))
				System.out
						.println("Warning : attempt to convert a Long (64bit) value into an Integer (32bit) and the result will be truncated.");
			return lv.intValue();
		} catch (NullPointerException e) {
			return 0;
		}
	}

	@Override
	public Long readLong(String colName) {
		try {
			return ((Number) feature.getAttribute(colName)).longValue();
		} catch (NullPointerException e) {
			return 0L;
		}
	}

	@Override
	public String readString(String colName) {
		try {
			return (String) feature.getAttribute(colName);
		} catch (NullPointerException e) {
			return "no data";
		}
	}

	@Override
	public Double readDouble(String colName) {
		try {
			return ((Number) feature.getAttribute(colName)).doubleValue();
		} catch (NullPointerException e) {
			return 0.0;
		}
	}

	@Override
	public Boolean readBoolean(String colName) {
		try {
			return ((Boolean) feature.getAttribute(colName)).booleanValue();
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public int getColumnIndexFromName(String colName) {
		return feature.getType().indexOf(colName);
	}

	public Point readPoint(MathTransform mt) {
		Point p = (Point) feature.getDefaultGeometry();
		if (mt != null)
			return p.transform(mt);
		else
			return p;
	}

	public Line readLine(MathTransform mt) {
		Line lin = (Line) feature.getDefaultGeometry();
		if (mt != null)
			return lin.transform(mt);
		else
			return lin;
	}

	public Ring readRing(MathTransform mt) {
		Ring r = (Ring) feature.getDefaultGeometry();
		if (mt != null)
			return r.transform(mt);
		return r;
	}

	public Polygon readPolygon(MathTransform mt) {
		Polygon pol = (Polygon) feature.getDefaultGeometry();
		if (mt != null)
			return pol.transform(mt);
		else
			return pol;
	}

	public MultiPoint readMultiPoint(MathTransform mt) {
		MultiPoint mp = (MultiPoint) feature.getDefaultGeometry();
		if (mt != null)
			return mp.transform(mt);
		else
			return mp;
	}

	public MultiLine readMultiLine(MathTransform mt) {
		MultiLine mlin = (MultiLine) feature.getDefaultGeometry();
		if (mt != null)
			return mlin.transform(mt);
		else
			return mlin;
	}

	public MultiPolygon readMultiPolygon(MathTransform mt) {
		MultiPolygon mpol = (MultiPolygon) feature.getDefaultGeometry();
		if (mt != null)
			return mpol.transform(mt);
		else
			return mpol;
	}

}
