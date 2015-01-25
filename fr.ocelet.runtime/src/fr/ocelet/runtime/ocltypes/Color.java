package fr.ocelet.runtime.ocltypes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An specialized version of awt.Color that provide its own color parser
 * capabilities.
 * 
 * @author Pascal Degenne - Initial contribution
 */
@SuppressWarnings("serial")
public class Color extends java.awt.Color {

	public Color() {
		super(200, 200, 200, 255);
	}

	public Color(int r, int g, int b) {
		super(r, g, b);
	}

	public Color(int r, int g, int b, int a) {
		super(r, g, b, a);
	}

	public static Color rgb(int r, int g, int b) {
		return new Color(r, g, b);
	}

	public static Color rgba(int r, int g, int b, int a) {
		return new Color(r, g, b, a);
	}

	/**
	 * Creates a Color from a textual description.
	 * 
	 * Accepted textual formats are :
	 * <ul>
	 * <li>rgb(red,green,blue) (0..255 values for each component)</li>
	 * <li>rgba(red,green,blue,alpha) (0..255 values for each component)</li>
	 * <li>#RRGGBB (hexadecimal pairs)</li>
	 * <li>#RRGGBBAA (hexadecimal pairs)</li>
	 * </ul>
	 * 
	 * @param scolor
	 *            Color described in String format
	 * @return A new Color or null if the text could not be recognized as
	 *         describing a color.
	 */
	public static Color text(String scolor) {
		return parse(scolor);
	}

	/**
	 * Creates a shade of colors between this color and another one given in
	 * argument
	 * 
	 * @param nbSteps
	 *            Number of colors (minimum is 2)
	 * @param c2
	 *            Last Color
	 * @return A List<Color> containing the color range
	 */
	public List<Color> colorRange(int nbSteps, Color c2) {
		List<Color> lc = new List<Color>();
		lc.add(this);
		double p = 1 / ((double) (nbSteps - 1));
		for (int i = 1; i < (nbSteps - 1); i++) {
			int r, g, b;
			r = getCompLevel(getRed(), c2.getRed(), p * i);
			g = getCompLevel(getGreen(), c2.getGreen(), p * i);
			b = getCompLevel(getBlue(), c2.getBlue(), p * i);
			lc.add(new Color(r, g, b));
		}
		lc.add(c2);
		return lc;
	}

	/**
	 * Parses a textual representation of a color and returns the corresponding
	 * Color object if parsing the string was successful.
	 * 
	 * Accepted textual formats are : rgb(red,green,blue) (0..255 values for
	 * each component) rgba(red,green,blue,alpha) (0..255 values for each
	 * component) #RRGGBB (hexadecimal pairs) #RRGGBBAA (hexadecimal pairs)
	 * 
	 * @param colstring
	 * @return A new Color object, or null if the text could not be decoded.
	 */
	public static Color parse(String colstring) {
		Color c = rgbColor(colstring);
		if (c != null)
			return c;
		c = rgbaColor(colstring);
		if (c != null)
			return c;
		c = hexColor(colstring);
		return c;
	}

	private int getCompLevel(int cp1, int cp2, double prop) {
		int lo = (cp1 >= cp2) ? cp2 : cp1;
		int hi = (cp1 < cp2) ? cp2 : cp1;
		Double v = (hi - lo) * prop;
		return (cp1 >= cp2) ? cp1 - v.intValue() : cp1 + v.intValue();
	}

	private static Color rgbColor(String expr) {
		Pattern pattern = Pattern
				.compile(" *rgb *\\( *([0-9]+) *, *([0-9]+) *, *([0-9]+) *\\) *");
		Matcher matcher = pattern.matcher(expr);
		if (matcher.matches()) {
			return new Color(Integer.valueOf(matcher.group(1)) % 256,
					Integer.valueOf(matcher.group(2)) % 256,
					Integer.valueOf(matcher.group(3)) % 256);
		} else
			return null;
	}

	private static Color rgbaColor(String expr) {
		Pattern pattern = Pattern
				.compile(" *rgba *\\( *([0-9]+) *, *([0-9]+) *, *([0-9]+) *, *([0-9]+) *\\) *");
		Matcher matcher = pattern.matcher(expr);
		if (matcher.matches()) {
			return new Color(Integer.valueOf(matcher.group(1)) % 256,
					Integer.valueOf(matcher.group(2)) % 256,
					Integer.valueOf(matcher.group(3)) % 256,
					Integer.valueOf(matcher.group(4)) % 256);
		} else
			return null;
	}

	private static Color hexColor(String expr) {
		Pattern hexpattern = Pattern
				.compile("#([0-9A-F]{2})([0-9A-F]{2})([0-9A-F]{2})([0-9A-F]{2})?");
		Matcher hexmatcher = hexpattern.matcher(expr);
		if (hexmatcher.matches()) {
			if (hexmatcher.group(4) == null)
				return new Color(Integer.parseInt(hexmatcher.group(1), 16),
						Integer.parseInt(hexmatcher.group(2), 16),
						Integer.parseInt(hexmatcher.group(3), 16));
			else
				return new Color(Integer.parseInt(hexmatcher.group(1), 16),
						Integer.parseInt(hexmatcher.group(2), 16),
						Integer.parseInt(hexmatcher.group(3), 16),
						Integer.parseInt(hexmatcher.group(4), 16));
		} else
			return null;
	}

	/**
	 * Creates a new Color that is darker than this Color using the proportion
	 * given in argument
	 * 
	 * @param prop
	 *            A proportion used to darken the color. Must be between 0 and
	 *            1.
	 * @return The new darker Color
	 */
	public Color darker(Double prop) {
		double p = prop > 1 ? 1 : 1 - prop;
		p = prop < 0 ? 1 : p;
		int r = getRed();
		int g = getGreen();
		int b = getBlue();

		r = (int) Math.max((double) r * p, 0);
		g = (int) Math.max((double) g * p, 0);
		b = (int) Math.max((double) b * p, 0);
		return new Color(r, g, b, getAlpha());
	}

	/**
	 * Creates a new Color that is lighter than this Color using the proportion
	 * given in argument. The transparency level is unchanged.
	 * 
	 * @param prop
	 *            A proportion used to darken the color. Must be between 0 and
	 *            1.
	 * @return The new darker Color
	 */
	public Color lighter(Double prop) {
		double p = prop > 1 ? 1 : 1 + prop;
		p = prop < 0 ? 1 : p;
		int r = getRed();
		int g = getGreen();
		int b = getBlue();

		r = (int) Math.min((double) r * p, 255);
		g = (int) Math.min((double) g * p, 255);
		b = (int) Math.min((double) b * p, 255);
		return new Color(r, g, b, getAlpha());
	}

	/**
	 * Returns a String representation of this color. The format is
	 * "rgb(red,breen,blue)" if the color is opaque or
	 * "rgba(red,green,blue,transparency)" if the color has a transparency
	 * level. All components are integer values between 0 and 255.
	 */
	public String toString() {
		if (this.getAlpha() < 255)
			return "rgba(" + getRed() + "," + getGreen() + "," + getBlue()
					+ "," + getAlpha() + ")";
		else
			return "rgb(" + getRed() + "," + getGreen() + "," + getBlue() + ")";
	}

}
