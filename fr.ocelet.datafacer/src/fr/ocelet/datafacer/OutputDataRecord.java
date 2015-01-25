package fr.ocelet.datafacer;

import org.opengis.feature.simple.SimpleFeature;

/**
 * Describes the behavior of all tabular data records that will be written to a data store
 * @author Pascal Degenne, Initial contribution
 *
 */
public interface OutputDataRecord {
	
	public void setAttribute(String atrName, Object value);
	public SimpleFeature getFeature();
}
