/*
*  Ocelet spatial modelling language.   www.ocelet.org
*  Copyright Cirad 2010-2018
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

package fr.ocelet.datafacer.ocltypes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import java.sql.*;

import org.geotools.data.DataStore;
import org.geotools.data.Query;
import org.geotools.data.postgis.PostgisNGDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.JDBCDataStore;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import fr.ocelet.datafacer.InputDataRecord;
import fr.ocelet.datafacer.GtDataRecord;
import fr.ocelet.datafacer.GtDatafacer;
import fr.ocelet.datafacer.InputDatafacer;
import fr.ocelet.datafacer.OutputDataRecord;
import fr.ocelet.datafacer.OutputDatafacer;
import fr.ocelet.runtime.geom.OceletGeomFactory;

/**
 * A datafacer designed to read and write tabular data in a PostreSQL and
 * PostGIS database.
 * 
 * @author Pascal Degenne, Initial contribution
 */
public abstract class Postgis extends GtDatafacer
		implements InputDatafacer, OutputDatafacer, Iterator<InputDataRecord> {

	private final String ERR_HEADER = "Datafacer Postgis: ";

	protected JDBCDataStore datastore;
	protected String schema;
	protected String base;
	protected String table;
	protected String host;
	protected String port;
	private String user;
	private String pwd;

	/**
	 * Constructor initializing the SGBD connection details, schema, table and the
	 * SRID.
	 * 
	 * @param epsgcode The coordinate system in text format. Ex: "EPSG:4326"
	 */
	public Postgis(String host, String port, String base, String schema, String table, String user, String pwd,
			String epsgcode) {
		super();
		if (datastore == null) {
			PostgisNGDataStoreFactory pgFactory = new PostgisNGDataStoreFactory();
			Map<String, String> jdbcparams = new HashMap<String, String>();
			jdbcparams.put(PostgisNGDataStoreFactory.DBTYPE.key, "postgis");
			jdbcparams.put(PostgisNGDataStoreFactory.HOST.key, host);
			jdbcparams.put(PostgisNGDataStoreFactory.PORT.key, port);
			jdbcparams.put(PostgisNGDataStoreFactory.SCHEMA.key, schema);
			jdbcparams.put(PostgisNGDataStoreFactory.DATABASE.key, base);
			jdbcparams.put(PostgisNGDataStoreFactory.USER.key, user);
			jdbcparams.put(PostgisNGDataStoreFactory.PASSWD.key, pwd);
			try {
				datastore = pgFactory.createDataStore(jdbcparams);
				this.host = host;
				this.port = port;
				this.base = base;
				this.schema = schema;
				this.table = table;
				this.user = user;
				this.pwd = pwd;
			} catch (IOException e) {
				System.out.println(ERR_HEADER
						+ "Failed to initialize the datafacer, please check the database connection parameters.");
			}
		}
		datastore.setGeometryFactory(new OceletGeomFactory());
		setCrs(epsgcode);
	}

	/**
	 * @returns The base, schema and table name
	 */
	public String toString() {
		return base + "." + schema + "." + table;
	}

	/**
	 * @return A short symbolic name for this data set
	 */
	@Override
	public String getName() {
		return table;
	}

	/**
	 * Creates a new empty datarecord.
	 * 
	 * @return An OutputDataRecord with no attribute initialization
	 */
	@Override
	public OutputDataRecord createOutputDataRec() {
		OutputDataRecord odr = null;
		try {
			odr = new PostgisDataRec(createFeature(getSimpleFeatureType()));
		} catch (IOException e) {
			System.out.println(getErrHeader()
					+ " Failed to create a record before writing to the datafacer. Please check the datafacer declaration.");
			return null;
		}
		return odr;
	}

	/**
	 * @return A complete description of everything we know about this Shapefile
	 * @deprecated Use about() instead
	 */
	public String getMetadata() {
		return about();
	}

	/**
	 * @return A complete description of everything we know about this Shapefile
	 */
	public String about() {
		StringBuffer sb = new StringBuffer();
		try {
			SimpleFeatureType sft = datastore.getSchema(table);
			sb.append("Table : " + sft.getTypeName() + "\n");
			sb.append("  Contains " + getFeatureSource().getCount(new Query()) + " records. \n");
			CoordinateReferenceSystem crs = sft.getCoordinateReferenceSystem();
			if (crs != null)
				sb.append("  Coordinate reference system : " + crs.getName() + "\n");
			ReferencedEnvelope bounds = getFeatureSource().getBounds();
			sb.append("  Bounds : " + bounds.getMinX() + " " + bounds.getMinY() + " , " + bounds.getMaxX() + " "
					+ bounds.getMaxY() + " \n");
			int nbat = sft.getAttributeCount();
			if (nbat == 1)
				sb.append("  Description of the only attribute :" + "\n");
			else
				sb.append("  Description of the " + nbat + " attributes :" + "\n");
			int adx = 0;
			for (AttributeDescriptor ad : sft.getAttributeDescriptors())
				sb.append("   [" + (1 + adx++) + "] : " + ad.getName() + " : \t"
						+ ad.getType().getBinding().getSimpleName() + "\n");
		} catch (IOException e) {
			System.out.println(ERR_HEADER + "Failed to access to " + this.toString());
			System.out.println("Caused by : " + e.getMessage());
		} catch (NullPointerException e) {
			System.out.println(ERR_HEADER + "Failed to access to " + this.toString());
			System.out.println("Caused by : " + e.getMessage());
		}
		return sb.toString();
	}

	@Override
	public Iterator<InputDataRecord> iterator() {
		return this;
	}

	/**
	 * Checks if there is any more record that has not yet been read
	 * 
	 * @return true is there are more records to be read
	 */
	@Override
	public boolean hasNext() {
		boolean res = false;
		if (sfiterator == null) {
			try {
				if (featureCollection == null) {
					if (getFeatureSource() != null)
						featureCollection = getFeatureSource().getFeatures();
					else
						return false;
				}
				sfiterator = featureCollection.features();
			} catch (IOException e) {
				System.out.println(ERR_HEADER + "Problem while attempting to read the table's content.");
				return false;
			}
		}

		res = sfiterator.hasNext();
		if (res == false) {
			sfiterator.close();
			sfiterator = null;
		}
		return res;
	}

	@Override
	public void remove() {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://" + host + ":" + port + "/" + base, user, pwd);
			stmt = conn.createStatement();
			stmt.executeUpdate("DROP TABLE \"" + table + "\"");
			stmt.close();
			conn.close();
		} catch (ClassNotFoundException | SQLException e) {
		}
	}

	/**
	 * Returns the next record read from the table.
	 * 
	 * @return A line String
	 */
	public GtDataRecord next() {
		GtDataRecord nextRecord = null;
		if (hasNext()) {
			SimpleFeature feature = sfiterator.next();
			nextRecord = new PostgisDataRec(feature);
			lastRead = nextRecord;
		}
		return nextRecord;
	}

	@Override
	public String getErrHeader() {
		return this.ERR_HEADER;
	}

	@Override
	public SimpleFeatureSource getFeatureSource() throws IOException {
		return datastore.getFeatureSource(table);
	}

	/**
	 * @return The DataStore of this Datafacer
	 */
	@Override
	public DataStore getDataStore() {
		return datastore;
	}

	@Override
	public void close() {
		datastore.dispose();
	}

}
