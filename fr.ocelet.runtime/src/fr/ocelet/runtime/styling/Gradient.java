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

package fr.ocelet.runtime.styling;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.ocelet.runtime.ocltypes.Color;
import fr.ocelet.runtime.ocltypes.KeyMap;
import fr.ocelet.runtime.ocltypes.List;

/**
 * Defines a gradient of colors.
 * This class can parse gradient definition files and build
 * lists of colors according to those definitions.
 * 
 * @author Pascal Degenne - Initial contribution
 */
public class Gradient {

	private TreeSet<Colorstop> gradient;
	private String name;
	public final static String REGEX_PATTERN = "\\s*(?:/\\*\\*?.*?\\*/)?\\s*(\\w*):linear-gradient\\(([a-z0-9 ]*)((?:\\s*,\\s*(?:"
			+ Colorstop.REGEX_PATTERN + "))+)(\\s*\\);)";

	public String getName() {
		return name;
	}

	public Gradient(String gradientdef) {
		gradient = new TreeSet<Colorstop>();
		this.name = parse(gradientdef);
	}

	private String parse(String grdef) {
		String name = null;
		Pattern pattern = Pattern.compile(REGEX_PATTERN, Pattern.DOTALL);
		Matcher mat1 = pattern.matcher(grdef);
		if (mat1.matches()) {
			Pattern pat2 = Pattern.compile(" *(?:" + Colorstop.REGEX_PATTERN
					+ ")");
			Matcher mat2 = pat2.matcher(mat1.group(3));
			while (mat2.find()) {
				Colorstop cs = Colorstop.parse(mat2.group());
				if (cs != null)
					gradient.add(cs);
			}
			name = mat1.group(1);
		}
		return name;
	}

	/**
	 * Produces a list of a given number of Colors from this Gradient
	 * 
	 * @param nbclasses
	 * @return A List<Color>
	 */
	public List<Color> toColorList(int nbclasses) {
		return toColorList(nbclasses, false);
	}

	/**
	 * Produces a list of a given number of Colors from this Gradient
	 * 
	 * @param nbclasses
	 * @return A List<Color>
	 */
	public List<Color> toReversedColorList(int nbclasses) {
		return toColorList(nbclasses, true);
	}

	/**
	 * Produces a list of a given number of Colors from this Gradient
	 * 
	 * @param nbclasses
	 * @return A List<Color>
	 */
	public List<Color> toColorList(int nbclasses, boolean isReverse) {

		List<Color> lc = new List<Color>();
		int nbc = nbclasses - 1;
		float t = 1 / (float) nbc;
		Colorstop inf, sup;
		Iterator<Colorstop> itcs = gradient.iterator();
		if (itcs.hasNext()) {
			inf = itcs.next();
			sup = inf;
			for (int i = 0; i < nbc; i++) {
				float ix = (float) i * t;
				while ((sup.compareTo(ix) < 0) && (itcs.hasNext())) {
					inf = sup;
					sup = itcs.next();
				}
				if (inf.compareTo(ix) >= 0)
					lc.add(inf.getColor());
				else if (sup.compareTo(ix) < 0)
					lc.add(sup.getColor());
				else {
					float ixp = (ix - inf.getPos())
							/ (sup.getPos() - inf.getPos());
					int infc = inf.getColor().getRed();
					int supc = sup.getColor().getRed();
					Float newRed = (infc * (1 - ixp) + supc * ixp);
					infc = inf.getColor().getGreen();
					supc = sup.getColor().getGreen();
					Float newGreen = (infc * (1 - ixp) + supc * ixp);
					infc = inf.getColor().getBlue();
					supc = sup.getColor().getBlue();
					Float newBlue = (infc * (1 - ixp) + supc * ixp);
					infc = inf.getColor().getAlpha();
					supc = sup.getColor().getAlpha();
					Float newAlpha = (infc * (1 - ixp) + supc * ixp);
					Color newc;
					if (newAlpha > 254) {
						newc = new Color(newRed.intValue(),
								newGreen.intValue(), newBlue.intValue());
					} else
						newc = new Color(newRed.intValue(),
								newGreen.intValue(), newBlue.intValue(),
								newAlpha.intValue());
					if (isReverse)
						lc.add(0, newc);
					else
						lc.add(newc);
				}
			}
			if (isReverse)
				lc.add(0, gradient.last().getColor());
			else
				lc.add(gradient.last().getColor());
		}
		return lc;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(name + ":linear-gradient(to right");
		for (Colorstop cst : gradient) {
			sb.append(",\n  " + cst);
		}
		sb.append("\n);");
		return sb.toString();
	}

	public static KeyMap<String, Gradient> readGradients(
			KeyMap<String, Gradient> gmap, String filePath)
			throws java.io.IOException {
		KeyMap<String, Gradient> gmapr = gmap;
		if (gmapr == null)
			gmapr = new KeyMap<String, Gradient>();
		byte[] buffer = new byte[(int) new File(filePath).length()];
		BufferedInputStream f = null;
		try {
			f = new BufferedInputStream(new FileInputStream(filePath));
			f.read(buffer);
		} finally {
			if (f != null)
				try {
					f.close();
				} catch (IOException ignored) {
				}
		}
		String gstring = new String(buffer);
		Pattern pat = Pattern.compile("\\s*(?:" + Gradient.REGEX_PATTERN + ")");
		Matcher mat = pat.matcher(gstring);
		while (mat.find()) {
			Gradient grad = new Gradient(mat.group());
			if (grad != null)
				gmapr.put(grad.getName(), grad);
		}
		return gmapr;
	}
	
}