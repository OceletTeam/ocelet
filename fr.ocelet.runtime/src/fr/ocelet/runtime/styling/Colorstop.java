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

package fr.ocelet.runtime.styling;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.ocelet.runtime.Miscutils;
import fr.ocelet.runtime.ocltypes.Color;

/**
 * One color elements for a gradient definition
 * 
 * @author Pascal Degenne - Initial contribution
 *
 */
public class Colorstop implements Comparable<Colorstop> {
	private final Color col;
	private final Float pos;
	public static String REGEX_PATTERN = "\\s*([r|#][gbaABCDEF0-9,\\(\\) ]*)( +[0-9]{1,3}(?:\\.[0-9]*)?)%";

	public Colorstop(Color c, Float p) {
		col = c;
		pos = p;
	}

	@Override
	public int compareTo(Colorstop cs) {
		int res = pos.compareTo(cs.pos);
		if ((res == 0) && (!col.equals(cs.col)))
			return 1;
		return res;
	}

	public int compareTo(Float cpos) {
		return pos.compareTo(cpos);
	}

	public Color getColor() {
		return col;
	}

	public Float getPos() {
		return pos;
	}

	public String toString() {
		return col + " " + Miscutils.format(pos, "#.##%");
	}

	public static Colorstop parse(String expr) {
		Pattern pattern = Pattern.compile(REGEX_PATTERN);
		Matcher matcher = pattern.matcher(expr);
		if (matcher.matches()) {
			Float pc = new Float(matcher.group(2)) / 100;
			return new Colorstop(Color.parse(matcher.group(1)), pc);
		}
		return null;
	}
}
