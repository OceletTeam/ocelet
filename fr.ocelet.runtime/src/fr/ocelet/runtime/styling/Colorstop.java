package fr.ocelet.runtime.styling;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.ocelet.runtime.Miscutils;
import fr.ocelet.runtime.ocltypes.Color;

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
		if ((res == 0) && (!col.equals(cs.col))) return 1; 
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
