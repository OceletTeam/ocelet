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
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geotools.data.Query;
import org.geotools.data.shapefile.ShapefileAttributeReader;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.prj.PrjFileReader;
import org.geotools.data.shapefile.shp.IndexFile;
import org.geotools.data.shapefile.shp.JTSUtilities;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.factory.Hints;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.type.BasicFeatureTypes;
import org.geotools.filter.visitor.ExtractBoundsFilterVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.renderer.ScreenMap;
import org.geotools.resources.Classes;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.filter.Filter;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ocelet.runtime.geom.OceletGeomFactory;

/**
 * A ShapefileDataStore that uses the OceletGeomFactory instead of the JTS GeometryFactory.
 * This class was designed to extend the V9.4 of Geotools original ShapefileDataStore.
 * 
 * @author Pascal Degenne, Initial contribution.
 *
 */
public class OcltShapefileDataStore extends
		org.geotools.data.shapefile.ShapefileDataStore {

	public OcltShapefileDataStore(URL url)
			throws java.net.MalformedURLException {
		super(url);
	}

	/**
	 * Builds the most appropriate geometry factory depending on the available
	 * query hints
	 * 
	 * @param query
	 * @return
	 */
	@Override
	protected GeometryFactory getGeometryFactory(Hints hints) {
		// if no hints, use the default geometry factory
		if (hints == null)
			return new OceletGeomFactory();

		// grab a geometry factory... check for a special hint
		GeometryFactory geometryFactory = (GeometryFactory) hints
				.get(Hints.JTS_GEOMETRY_FACTORY);
		if (geometryFactory == null) {
			// look for a coordinate sequence factory
			CoordinateSequenceFactory csFactory = (CoordinateSequenceFactory) hints
					.get(Hints.JTS_COORDINATE_SEQUENCE_FACTORY);

			if (csFactory != null) {
				geometryFactory = new GeometryFactory(csFactory);
			}
		}

		if (geometryFactory == null) {
			// fall back on the default one
			geometryFactory = new OceletGeomFactory();
		}
		return geometryFactory;
	}

	/**
	 * Returns the attribute reader, allowing for a pure shapefile reader, or a
	 * combined dbf/shp reader.
	 * 
	 * @param readDbf
	 *            - if true, the dbf fill will be opened and read
	 * 
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected ShapefileAttributeReader getAttributesReader(boolean readDbf,
			Query q) throws IOException {

		List<AttributeDescriptor> atts = (schema == null) ? readAttributes()
				: schema.getAttributeDescriptors();

		GeometryFactory geometryFactory;
		if (q != null) {
			geometryFactory = getGeometryFactory(q.getHints());
		} else {
			geometryFactory = new OceletGeomFactory();
		}

		ShapefileAttributeReader result;
		ShapefileReader shapeReader = openShapeReader(geometryFactory);
		if (!readDbf) {
			LOGGER.fine("The DBF file won't be opened since no attributes will be read from it");
			atts = new ArrayList(1);
			atts.add(schema.getGeometryDescriptor());
			result = new ShapefileAttributeReader(atts, shapeReader, null);
		} else {
			result = new ShapefileAttributeReader(atts, shapeReader,
					openDbfReader());
		}

		// setup the target bbox if any, and the generalization hints if
		// available
		if (q != null) {
			Envelope bbox = new ReferencedEnvelope();
			bbox = (Envelope) q.getFilter().accept(
					ExtractBoundsFilterVisitor.BOUNDS_VISITOR, bbox);
			if (bbox != null && !bbox.isNull()) {
				result.setTargetBBox(bbox);
			}

			Hints hints = q.getHints();
			if (hints != null) {
				Number simplificationDistance = (Number) hints
						.get(Hints.GEOMETRY_DISTANCE);
				if (simplificationDistance != null) {
					result.setSimplificationDistance(simplificationDistance
							.doubleValue());
				}
				result.setScreenMap((ScreenMap) hints.get(Hints.SCREENMAP));

				if (Boolean.TRUE.equals(hints.get(Hints.FEATURE_2D))) {
					shapeReader.setFlatGeometry(true);
				}
			}
		}
		return result;
	}

	/**
	 * Create the AttributeDescriptor contained within this DataStore.
	 * 
	 * @return List of new AttributeDescriptor
	 * @throws IOException
	 *             If AttributeType reading fails
	 */
	@SuppressWarnings("rawtypes")
	protected List<AttributeDescriptor> readAttributes() throws IOException {
		ShapefileReader shp = openShapeReader(new OceletGeomFactory());
		DbaseFileReader dbf = openDbfReader();
		CoordinateReferenceSystem crs = null;

		PrjFileReader prj = null;
		try {
			prj = openPrjReader();

			if (prj != null) {
				crs = prj.getCoodinateSystem();
			}
		} catch (FactoryException fe) {
			crs = null;
		}

		AttributeTypeBuilder build = new AttributeTypeBuilder();
		List<AttributeDescriptor> attributes = new ArrayList<AttributeDescriptor>();
		try {
			Class<?> geometryClass = JTSUtilities.findBestGeometryClass(shp
					.getHeader().getShapeType());
			build.setName(Classes.getShortName(geometryClass));
			build.setNillable(true);
			build.setCRS(crs);
			build.setBinding(geometryClass);

			GeometryType geometryType = build.buildGeometryType();
			attributes.add(build.buildDescriptor(
					BasicFeatureTypes.GEOMETRY_ATTRIBUTE_NAME, geometryType));
			Set<String> usedNames = new HashSet<String>(); // record names in
			// case of
			// duplicates
			usedNames.add(BasicFeatureTypes.GEOMETRY_ATTRIBUTE_NAME);

			// take care of the case where no dbf and query wants all =>
			// geometry only
			if (dbf != null) {
				DbaseFileHeader header = dbf.getHeader();
				for (int i = 0, ii = header.getNumFields(); i < ii; i++) {
					Class attributeClass = header.getFieldClass(i);
					String name = header.getFieldName(i);
					if (usedNames.contains(name)) {
						String origional = name;
						int count = 1;
						name = name + count;
						while (usedNames.contains(name)) {
							count++;
							name = origional + count;
						}
						build.addUserData(ORIGINAL_FIELD_NAME, origional);
						build.addUserData(ORIGINAL_FIELD_DUPLICITY_COUNT, count);
					}
					usedNames.add(name);
					int length = header.getFieldLength(i);

					build.setNillable(true);
					build.setLength(length);
					build.setBinding(attributeClass);
					attributes.add(build.buildDescriptor(name));
				}
			}
			return attributes;
		} finally {

			try {
				if (prj != null) {
					prj.close();
				}
			} catch (IOException ioe) {
				// do nothing
			}
			try {
				if (dbf != null) {
					dbf.close();
				}
			} catch (IOException ioe) {
				// do nothing
			}
			try {
				if (shp != null) {
					shp.close();
				}
			} catch (IOException ioe) {
				// do nothing
			}
		}
	}

	/**
	 * @see org.geotools.data.AbstractDataStore#getCount(org.geotools.data.Query)
	 */
	public int getCount(Query query) throws IOException {
		if (query.getFilter() == Filter.INCLUDE) {
			IndexFile file = openIndexFile();
			if (file != null) {
				try {
					return file.getRecordCount();
				} finally {
					file.close();
				}
			}

			// no Index file so use the number of shapefile records
			ShapefileReader reader = openShapeReader(new OceletGeomFactory());
			int count = -1;
			try {
				count = reader.getCount(count);
			} catch (IOException e) {
				throw e;
			} finally {
				reader.close();
			}
			return count;
		}
		return super.getCount(query);
	}

}
