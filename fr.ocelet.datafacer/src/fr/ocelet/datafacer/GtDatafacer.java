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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.geotools.data.DataStore;
import org.geotools.data.FeatureStore;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.Hints;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import fr.ocelet.runtime.entity.Entity;
import fr.ocelet.runtime.geom.SpatialManager;
import fr.ocelet.runtime.geom.ocltypes.Line;
import fr.ocelet.runtime.geom.ocltypes.MultiLine;
import fr.ocelet.runtime.geom.ocltypes.MultiPoint;
import fr.ocelet.runtime.geom.ocltypes.MultiPolygon;
import fr.ocelet.runtime.geom.ocltypes.Point;
import fr.ocelet.runtime.geom.ocltypes.Polygon;
import fr.ocelet.runtime.geom.ocltypes.Ring;

/**
 * Geotools based datafacers common code
 * 
 * @author Pascal Degenne - Initial contribution
 *
 */
public abstract class GtDatafacer implements InputDatafacer, OutputDatafacer,
		FiltrableDatafacer {

	protected MathTransform mt;
	protected CoordinateReferenceSystem crs;
	protected SimpleFeatureIterator sfiterator;

	protected SimpleFeatureCollection featureCollection;
	protected GtDataRecord lastRead;
	protected String cqlFilter;

	public GtDataRecord getLastRead() {
		return this.lastRead;
	}

	public abstract String getErrHeader();

	/**
	 * This method is not declared abstract to make sure it always exists. But
	 * its real behavior is given by the same method in a generated subclass of
	 * this datafacer
	 * 
	 * @return null here but this method is expected to be overriden by
	 *         generated code
	 */
	protected HashMap<String, String> getMatchdef() {
		return null;
	}

	/**
	 * @return A short symbolic name for this data set
	 */
	public abstract String getName();

	/**
	 * @return A SimpleFeatureSource according to the datastore used.
	 */
	public abstract SimpleFeatureSource getFeatureSource() throws IOException;

	/**
	 * @return The DataStore of this Datafacer
	 */
	public abstract DataStore getDataStore();

	/**
	 * Creates an initialized GtDataRecord from the properties of the Entity
	 * given in argument. The conversions from properties to record attributes
	 * is inferred from a match definition and is realized by a generated
	 * subclass of this one. The implementation given here is only to make sure
	 * the method is implemented in case a match is missing in the datafacer
	 * declaration.
	 * 
	 * @param ety
	 * @return A GtDataRecord with a feature initialized with a series of
	 *         attributes.
	 */
	public OutputDataRecord createRecord(Entity ety) {
		return null;
	}

	/**
	 * Obtains a SimpleFeatureType that represents the table schema of this
	 * datafacer. If that schema is already known, typically after reading from
	 * a Shapefile or a Postgis database, it is returned. If that schema is not
	 * already known, when building a table from scratch, then we create one
	 * inferred from the match definition found in the datafacer's declaration.
	 * 
	 * @return A SimpleFeatureType if one could be obtained
	 * @throws IOException
	 */
	public SimpleFeatureType getSimpleFeatureType() throws IOException {
		SimpleFeatureType sft = null;
		try {
			sft = getFeatureSource().getSchema();
		} catch (IOException ioe) {
			SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
			builder.setName(getName());
			if (crs != null)
				builder.setCRS(crs);
			HashMap<String, String> matchmap = getMatchdef();
			if (matchmap != null) {
				for (String atrName : matchmap.keySet()) {
					try {
						Class atrClass = Class.forName(matchmap.get(atrName));
						builder.add(atrName, atrClass);
					} catch (ClassNotFoundException cnfe) {
						System.out.println(getErrHeader()
								+ " Failed to add the attribute definition ("
								+ atrName + ":" + matchmap.get(atrName)
								+ ") when building the schema.");
					}
				}
				sft = builder.buildFeatureType();
				// If we are here, it means we did not have a schema for the
				// DataStore
				// As we just built one, we can affect it to our DataStore
				DataStore ds = getDataStore();
				if (ds != null)
					ds.createSchema(sft);
			}
		}
		return sft;
	}

	/**
	 * Implementation of the FiltrableDatafacer interface
	 * 
	 * @param cqlFilter
	 */
	@Override
	public void setFilter(String cqlFilter) {
		this.cqlFilter = cqlFilter;
	}

	/**
	 * Creates a new empty feature according to the feature type given in
	 * argument
	 * 
	 * @param ftype
	 *            A featureType representing the schema of the feature to create
	 * @return An empty SimpleFeature
	 */
	protected SimpleFeature createFeature(SimpleFeatureType ftype) {
		SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(ftype);
		SimpleFeature feature = featureBuilder.buildFeature(null);
		return feature;
	}

	private ListFeatureCollection getLfc() throws IOException {
		if (featureCollection == null)
			return new ListFeatureCollection(getSimpleFeatureType());
		else
			return new ListFeatureCollection(featureCollection);
	}

	/**
	 * Uses the entity given in argument to create a record and adds that record
	 * to the list of record to be written at next save call.
	 * 
	 * @param ety
	 *            The entity to add
	 */
	public void append(Entity ety) {
		try {
			ListFeatureCollection lfc = getLfc();
			OutputDataRecord gdr = createRecord(ety);
			if (gdr != null) {
				lfc.add(gdr.getFeature());
				commitWrite(lfc);
			}
		} catch (IOException | IllegalArgumentException e) {
			System.err
					.println(getErrHeader()
							+ " Failed to write an entity to "
							+ this.toString()
							+ ". Please check the datafacer's definition in your model.");
		}
	}

	/**
	 * Adds the records given in argument to the table of this datafacer. The
	 * records will be added to the table, no existing record will be changed.
	 * This method is called by the append(List <? extends Entity) method from a
	 * generated subclass which is in charge of creating specialized DataRecords
	 * from a list of Entities.
	 * 
	 * @param lety
	 *            The list of entities to be added
	 */
	public void append(List<? extends Entity> lrec) {
		try {
			ListFeatureCollection lfc = getLfc();
			ArrayList<SimpleFeature> lfeatures = new ArrayList<SimpleFeature>();
			for (Entity ety : lrec)
				lfeatures.add(createRecord(ety).getFeature());
			lfc.addAll(lfeatures);
			commitWrite(lfc);
		} catch (IOException | IllegalArgumentException e) {
			System.err
					.println(getErrHeader()
							+ " Failed to write an entity to "
							+ this.toString()
							+ ". Please check the datafacer's definition in your model.");
		}
	}

	private void commitWrite(ListFeatureCollection lfc) throws IOException {
		SimpleFeatureSource sfs = getFeatureSource();
		FeatureStore newFeatureStore = (FeatureStore) sfs;
		Transaction t = newFeatureStore.getTransaction();
		newFeatureStore.addFeatures(lfc);
		t.commit();
		t.close();
	}

	/**
	 * Decodes the EPSG String to obtain the corresponding CRS and obtains a
	 * MathTransform if the model's CRS is different.
	 * 
	 * @param epsg
	 *            The coordinate system in text format. Ex: "EPSG:4326"
	 */
	protected void setCrs(String epsgCode) {
		try {
			crs = CRS.decode(epsgCode);
			Hints.putSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, 
					Boolean.TRUE);
			

			mt = SpatialManager.getTransformCrs(crs, getErrHeader());
			
		} catch (NoSuchAuthorityCodeException e) {
			System.out.println(getErrHeader() + "Unknown EPSG code : "
					+ epsgCode);
		} catch (FactoryException e) {
			System.out.println(getErrHeader()
					+ "Failed to build the coordinate system :" + epsgCode);
			e.printStackTrace();
		}
	}

	/**
	 * Used to rewind the record iterator when calling readAll() several times.
	 */
	public void resetIterator() {
		if (sfiterator != null)
			sfiterator.close();
		sfiterator = null;
		featureCollection = null;
	}

	public Point readPoint() {
		return lastRead.readPoint(mt);
	}

	public Line readLine() {
		return lastRead.readLine(mt);
	}

	public Ring readRing() {
		return lastRead.readRing(mt);
	}

	public Polygon readPolygon() {
		return lastRead.readPolygon(mt);
	}

	public MultiPoint readMultiPoint() {
		return lastRead.readMultiPoint(mt);
	}

	public MultiLine readMultiLine() {
		return lastRead.readMultiLine(mt);
	}

	public MultiPolygon readMultiPolygon() {
		return lastRead.readMultiPolygon(mt);
	}

	public Point readPoint(String colname) {
		return readPoint();
	}

	public Point readPoint(Integer colnum) {
		return readPoint();
	}

	public Ring readRing(String colname) {
		return readRing();
	}

	public Ring readRing(Integer colnum) {
		return readRing();
	}

	public Line readLine(String colname) {
		return readLine();
	}

	public Line readLine(Integer colnum) {
		return readLine();
	}

	public Polygon readPolygon(String colname) {
		return readPolygon();
	}

	public Polygon readPolygon(Integer colnum) {
		return readPolygon();
	}

	public MultiPoint readMultiPoint(String colname) {
		return readMultiPoint();
	}

	public MultiPoint readMultiPoint(Integer colnum) {
		return readMultiPoint();
	}

	public MultiLine readMultiLine(String colname) {
		return readMultiLine();
	}

	public MultiLine readMultiLine(Integer colnum) {
		return readMultiLine();
	}

	public MultiPolygon readMultiPolygon(String colname) {
		return readMultiPolygon();
	}

	public MultiPolygon readMultiPolygon(Integer colnum) {
		return readMultiPolygon();
	}

	/**
	 * Obtains the value from one column of the last read record using the
	 * column number.
	 * 
	 * @param colNumber
	 *            Index of the column to read (first is #0)
	 * @return The value of the column as a String value
	 */
	public String readString(int colNumber) {
		return getLastRead().readString(colNumber);
	}

	/**
	 * Obtains the value from one column of the last read record using the
	 * column number.
	 * 
	 * @param colNumber
	 *            Index of the column to read (first is #0)
	 * @return The value of the column as an Integer value
	 */
	public Integer readInteger(int colNumber) {
		return getLastRead().readInteger(colNumber);
	}

	/**
	 * Obtains the value from one column of the last read record using the
	 * column number.
	 * 
	 * @param colNumber
	 *            Index of the column to read (first is #0)
	 * @return The value of the column as a Long integer value
	 */
	public Long readLong(int colNumber) {
		return getLastRead().readLong(colNumber);
	}

	/**
	 * Obtains the value from one column of the last read record using the
	 * column number.
	 * 
	 * @param colNumber
	 *            Index of the column to read (first is #0)
	 * @return The value of the column as a Doule value
	 */
	public Double readDouble(int colNumber) {
		return getLastRead().readDouble(colNumber);
	}

	/**
	 * Obtains the value from one column of the last read record using the
	 * column number.
	 * 
	 * @param colNumber
	 *            Index of the column to read (first is #0)
	 * @return The value of the column as a Boolean value
	 */
	public Boolean readBoolean(int colNumber) {
		return getLastRead().readBoolean(colNumber);
	}

	/**
	 * Obtains the value from one column of the last read record using the
	 * column name.
	 * 
	 * @param colName
	 *            Name of the column to read (case sensitive)
	 * @return The value of the column as a String value
	 */
	public String readString(String colName) {
		return getLastRead().readString(colName);
	}

	/**
	 * Obtains the value from one column of the last read record using the
	 * column name.
	 * 
	 * @param colName
	 *            Name of the column to read (case sensitive)
	 * @return The value of the column as an Integer value
	 */
	public Integer readInteger(String colName) {
		return getLastRead().readInteger(colName);
	}

	/**
	 * Obtains the value from one column of the last read record using the
	 * column name.
	 * 
	 * @param colName
	 *            Name of the column to read (case sensitive)
	 * @return The value of the column as a Long Integer value
	 */
	public Long readLong(String colName) {
		return getLastRead().readLong(colName);
	}

	/**
	 * Obtains the value from one column of the last read record using the
	 * column name.
	 * 
	 * @param colName
	 *            Name of the column to read (case sensitive)
	 * @return The value of the column as a Doule value
	 */
	public Double readDouble(String colName) {
		return getLastRead().readDouble(colName);
	}

	/**
	 * Obtains the value from one column of the last read record using the
	 * column name.
	 * 
	 * @param colName
	 *            Name of the column to read (case sensitive)
	 * @return The value of the column as a Boolean value
	 */
	public Boolean readBoolean(String colName) {
		return getLastRead().readBoolean(colName);
	}

}
