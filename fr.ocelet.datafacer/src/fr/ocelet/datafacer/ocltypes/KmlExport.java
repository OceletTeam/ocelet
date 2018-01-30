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

package fr.ocelet.datafacer.ocltypes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import com.vividsolutions.jts.geom.Geometry;

import de.micromata.opengis.kml.v_2_2_0.ColorMode;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.IconStyle;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Style;
import fr.ocelet.datafacer.Datafacer;
import fr.ocelet.datafacer.KmlFolder;
import fr.ocelet.datafacer.KmlUtils;
import fr.ocelet.runtime.Miscutils;
import fr.ocelet.runtime.ocltypes.Color;
import fr.ocelet.runtime.ocltypes.Date;
import fr.ocelet.runtime.ocltypes.DateTime;
import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.util.FileUtils;

/**
 * Datafacer dedicated to generating KML files
 *
 * @author Pascal Degenne - Initial contribution
 *
 */
@SuppressWarnings("restriction")
public class KmlExport implements Datafacer {

	private final String ERR_HEADER = "Datafacer KmlExport: ";
	// private final String MAX_HEIGHT_STYLE = "Max_Height_Style";
	private Kml kml;
	private Document doc;
	// private MathTransform mt;
	private KmlFolder defolder; // Default folder in case we need one.
	private HashMap<String, KmlFolder> folders;
	private String filename;
	private boolean isValidFileName;

	@Override
	public String getErrHeader() {
		return this.ERR_HEADER;
	}

	public KmlExport(String fileName) {
		setFileName(fileName);
		kml = new Kml();
		doc = kml.createAndSetDocument();
		folders = new HashMap<String, KmlFolder>();
	}

	public KmlFolder addFolder(String label, String beginDate, String endDate) {
		KmlFolder kf = new KmlFolder(doc, label, beginDate, endDate);
		folders.put(label, kf);
		return kf;
	}

	/**
	 * @deprecated Please use the DateTime version
	 */
	@Deprecated
	public KmlFolder addFolder(String label, Date beginDate, Date endDate) {
		KmlFolder kf = new KmlFolder(doc, label, dateToKml(beginDate),
				dateToKml(endDate));
		folders.put(label, kf);
		return kf;
	}

	public KmlFolder addFolder(String label, DateTime beginDate, DateTime endDate) {
		KmlFolder kf = new KmlFolder(doc, label, dateToKml(beginDate),
				dateToKml(endDate));
		folders.put(label, kf);
		return kf;
	}

	public void addLabel(double xpos, double ypos, double height,
			String beginDate, String endDate, String name, String description,
			String styleName) {
		getDefaultFolder().addLabel(xpos, ypos, height, beginDate, endDate,
				name, description, styleName);
	}

	/**
	 * @deprecated Please use the DateTime version
	 */
	@Deprecated
	public void addLabel(double xpos, double ypos, double height,
			Date beginDate, Date endDate, String name, String description,
			String styleName) {
		getDefaultFolder().addLabel(xpos, ypos, height, dateToKml(beginDate),
				dateToKml(endDate), name, description, styleName);
	}

	public void addLabel(double xpos, double ypos, double height,
			DateTime beginDate, DateTime endDate, String name, String description,
			String styleName) {
		getDefaultFolder().addLabel(xpos, ypos, height, dateToKml(beginDate),
				dateToKml(endDate), name, description, styleName);
	}

	public void addLabel(String foldname, double xpos, double ypos,
			double height, String beginDate, String endDate, String name,
			String description, String styleName) {
		getFolder(foldname).addLabel(xpos, ypos, height, beginDate, endDate,
				name, description, styleName);
	}

	/**
	 * @deprecated Please use the DateTime version
	 */
	@Deprecated
	public void addLabel(String foldname, double xpos, double ypos,
			double height, Date beginDate, Date endDate, String name,
			String description, String styleName) {
		getFolder(foldname).addLabel(xpos, ypos, height, dateToKml(beginDate),
				dateToKml(endDate), name, description, styleName);
	}

	public void addLabel(String foldname, double xpos, double ypos,
			double height, DateTime beginDate, DateTime endDate, String name,
			String description, String styleName) {
		getFolder(foldname).addLabel(xpos, ypos, height, dateToKml(beginDate),
				dateToKml(endDate), name, description, styleName);
	}

	public void add3DModel(double xpos, double ypos, double orientation,
			double scale, String beginDate, String endDate, String daefile) {
		getDefaultFolder().add3DModel(xpos, ypos, orientation, scale,
				beginDate, endDate, daefile);
	}

	/**
	 * @deprecated Please use the DateTime version
	 */
	@Deprecated
	public void add3DModel(double xpos, double ypos, double orientation,
			double scale, Date beginDate, Date endDate, String daefile) {
		getDefaultFolder().add3DModel(xpos, ypos, orientation, scale,
				dateToKml(beginDate), dateToKml(endDate), daefile);
	}

	public void add3DModel(double xpos, double ypos, double orientation,
			double scale, DateTime beginDate, DateTime endDate, String daefile) {
		getDefaultFolder().add3DModel(xpos, ypos, orientation, scale,
				dateToKml(beginDate), dateToKml(endDate), daefile);
	}

	public void add3DModel(String foldname, double xpos, double ypos,
			double orientation, double scale, String beginDate, String endDate,
			String daefile) {
		getFolder(foldname).add3DModel(xpos, ypos, orientation, scale,
				beginDate, endDate, daefile);
	}

	/**
	 * @deprecated Please use the DateTime version
	 */
	@Deprecated
	public void add3DModel(String foldname, double xpos, double ypos,
			double orientation, double scale, Date beginDate, Date endDate,
			String daefile) {
		getFolder(foldname).add3DModel(xpos, ypos, orientation, scale,
				dateToKml(beginDate), dateToKml(endDate), daefile);
	}

	public void add3DModel(String foldname, double xpos, double ypos,
			double orientation, double scale, DateTime beginDate, DateTime endDate,
			String daefile) {
		getFolder(foldname).add3DModel(xpos, ypos, orientation, scale,
				dateToKml(beginDate), dateToKml(endDate), daefile);
	}

	public void addGeometry(String label, String beginDate, String endDate,
			Geometry geom, String styleName, double height) {
		getDefaultFolder().addGeometry(label, beginDate, endDate, geom,
				styleName, height);
	}

	/**
	 * @deprecated Please use the DateTime version
	 */
	@Deprecated
	public void addGeometry(String label, Date beginDate, Date endDate,
			Geometry geom, String styleName, double height) {
		getDefaultFolder().addGeometry(label, dateToKml(beginDate),
				dateToKml(endDate), geom, styleName, height);
	}

	public void addGeometry(String label, DateTime beginDate, DateTime endDate,
			Geometry geom, String styleName, double height) {
		getDefaultFolder().addGeometry(label, dateToKml(beginDate),
				dateToKml(endDate), geom, styleName, height);
	}

	/**
	 * Adds a geometry to a Kml folder.
	 * 
	 * @param foldname
	 *            Label of the kml folder
	 * @param label
	 *            Label of the geometry legend
	 * @param beginDate
	 *            First date of display.
	 * @param endDate
	 *            End display date.
	 * @param geom
	 *            The geometry to add.
	 * @param styleName
	 *            Name of the drawing style to apply.
	 * @param height
	 *            Altitude or thickness of the geometry (depending of the
	 *            geometry kind).
	 */
	public void addGeometry(String foldname, String label, String beginDate,
			String endDate, Geometry geom, String styleName, double height) {
		getFolder(foldname).addGeometry(label, beginDate, endDate, geom,
				styleName, height);
	}

	/**
	 * @deprecated Please use the DateTime version
	 */
	@Deprecated
	public void addGeometry(String foldname, String label, Date beginDate,
			Date endDate, Geometry geom, String styleName, double height) {
		getFolder(foldname).addGeometry(label, dateToKml(beginDate),
				dateToKml(endDate), geom, styleName, height);
	}

	public void addGeometry(String foldname, String label, DateTime beginDate,
			DateTime endDate, Geometry geom, String styleName, double height) {
		getFolder(foldname).addGeometry(label, dateToKml(beginDate),
				dateToKml(endDate), geom, styleName, height);
	}

	
	/**
	 * Defines a new style to be used with addStyledRecord
	 * 
	 * @param name
	 *            Style name. Must be unique.
	 * @param lineWidth
	 *            Width of the line. A thin line should have a value 1.0.
	 * @param lineColor
	 *            Color of the line in HEX text format : ABGR. Example
	 *            "7FFFFF44" is half transparent yellow.
	 * @param fillColor
	 *            Polygon fill color. HEX text format ABGR also.
	 */
	public void defStyle(String name, double lineWidth, String lineColor,
			String fillColor) {
		Style style = doc.createAndAddStyle().withId(name);
		style.createAndSetLineStyle().withColor(lineColor).withWidth(lineWidth);
		style.createAndSetPolyStyle().withColor(fillColor)
				.withColorMode(ColorMode.NORMAL);
	}

	/**
	 * Defines a new style to be used with addStyledRecord
	 * 
	 * @param name
	 *            Style name. Must be unique.
	 * @param lineWidth
	 *            Width of the line. A thin line should have a value 1.0.
	 * @param lineColor
	 *            Color of the line.
	 * @param fillColor
	 *            Polygon fill color.
	 */
	public void defStyle(String name, double lineWidth, Color lineColor,
			Color fillColor) {
		Style style = doc.createAndAddStyle().withId(name);
		style.createAndSetLineStyle().withColor(kmlColor(lineColor))
				.withWidth(lineWidth);
		style.createAndSetPolyStyle().withColor(kmlColor(fillColor))
				.withColorMode(ColorMode.NORMAL);
	}

	/**
	 * Defines a whole range of styles using a name prefix. There will be as
	 * many styles as Colors found in the color list
	 * 
	 * @param prefix
	 *            String style prefix name. The number of style will be added to
	 *            this prefix starting at 1. Ex : "sty1", "sty2", "sty3", ...
	 * @param linewidth
	 *            Width of the line. A thin line should have a value 1.0.
	 * @param fillColors
	 *            Polygon fill color.
	 * @param lineColorProp
	 *            Proportion used to alter the fillColors to generate the line
	 *            colors. The value of the proportion must be between -1 and +1.
	 *            Negative values will produce darker shades of the fill colors
	 *            while positive values will produces lighter colors.
	 */
	public void defStyleRange(String prefix, Double linewidth,
			List<Color> fillColors, Double lineColorProp) {
		int index = 1;
		for (Color col : fillColors) {
			Color linec = lineColorProp >= 0.0 ? col.lighter(lineColorProp)
					: col.darker(-lineColorProp);
			defStyle(prefix + index, linewidth, linec, col);
			index++;
		}
	}

	/**
	 * Produces a Kml String representation of the Color given in argument
	 * 
	 * @param c
	 *            A Color
	 * @return A String in ABGR Hex text format
	 */
	public String kmlColor(Color c) {
		return KmlUtils.kmlColorRGBA(c.getRed(), c.getGreen(), c.getBlue(),
				c.getAlpha());
	}

	/**
	 * Defines a new IconStyle to be used with addStyledRecord
	 * 
	 * @param name
	 *            Style name. Must be unique.
	 * @param iconFile
	 *            Name of an image (png) file. The path is relative to the
	 *            location where the KML file will be saved.
	 * @param scale
	 *            Scale factor applied to the image. Choose 1.0 if you don't
	 *            know.
	 * @param heading
	 *            Orientation heading of the icon. Should be a value between 0.0
	 *            and 360.0. 0.0 is the normal orientation of the icon. Higher
	 *            numbers apply a clockwise rotation of the icon.
	 */

	public void defIconStyle(String name, String iconFile, double scale,
			double heading) {
		Style style = doc.createAndAddStyle().withId(name);
		IconStyle iconstyle = style.createAndSetIconStyle().withScale(scale)
				.withHeading(heading);
		iconstyle.createAndSetIcon().withHref(iconFile);
	}

	public void saveAsKml(String newFileName) {
		setFileName(newFileName);
		saveAsKml();
	}

	public void saveAsKmz(String newFileName) {
		setFileName(newFileName);
		saveAsKmz();
	}

	public void saveAsKml() {
		try {
			if (isValidFileName)
				kml.marshal(new File(filename));
			else
				System.out
						.println("Failed to save the kml file : no valid file name was provided.");
		} catch (FileNotFoundException e) {
			System.out.println(ERR_HEADER + "Failed to open " + filename
					+ " for saving.");
		}
	}

	public void saveAsKmz() {
		try {
			if (isValidFileName)
				kml.marshalAsKmz(filename);
			else
				System.out
						.println("Failed to save the kmz file : no valid file name was provided.");
		} catch (IOException e) {
			System.out.println(ERR_HEADER + "Failed to open " + filename
					+ " for saving.");
		}
	}

	public void setFileName(String newFileName) {
		isValidFileName = !newFileName.isEmpty();
		if (isValidFileName)
			this.filename = FileUtils.applyOutput(newFileName);
	}

   public void hideFolder(String folname) {
	   KmlFolder kf = getFolder(folname);
	   kf.setVisibility(false);
   }	
	
   public void showFolder(String folname) {
	   KmlFolder kf = getFolder(folname);
	   kf.setVisibility(true);
   }	
	
	public void remove() {
		Miscutils.removeFile(filename);
	}
	
	protected KmlFolder getDefaultFolder() {
		if (defolder == null) {
			defolder = new KmlFolder(doc, filename);
			folders.put(filename, defolder);
		}
		return defolder;
	}

	protected KmlFolder getFolder(String folname) {
		KmlFolder kf = folders.get(folname);
		if (kf == null) {
			kf = new KmlFolder(doc, folname);
			folders.put(folname, kf);
		}
		return kf;

	}

	protected String dateToKml(Date d) {
		return d.toString("yyyy-MM-dd") + "T" + d.toString("HH:mm:ss");
	}

	protected String dateToKml(DateTime d) {
		return d.toString("yyyy-MM-dd") + "T" + d.toString("HH:mm:ss");
	}
}
